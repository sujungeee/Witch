package com.ssafy.witch.Auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.model.dto.JoinUser
import com.ssafy.witch.data.model.dto.Login
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.remote.AuthService
import com.ssafy.witch.data.remote.RetrofitUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class LoginFragmentViewModel(application: Application): AndroidViewModel(application) {

    //뷰모델에서만 쓰는 것
    private val _joinUser = MutableLiveData<JoinUser>()
    //뷰모델 외 사용 가능한 public 선언
    val joinUser:LiveData<JoinUser>
        get() = _joinUser

    val sharedPreferencesUtil = SharedPreferencesUtil(application.applicationContext)

    // 이메일, 닉네임, JWT 토큰 로그인 처리
    fun login(email:String, password: String, onResult: (Boolean, String?) -> Unit) {
        //API 호출은 IO 스레드에서 실행
        viewModelScope.launch(Dispatchers.IO) {
            // login 위해 retrofit 으로 가야 함

            val authService = ApplicationClass.retrofit.create(AuthService::class.java)

            // LoginService에 있다
            runCatching {
                authService.login(Login(email, password))
            }.onSuccess { response ->
                //response 를 받아서 _User 에 담아주면 됨
                if (response.isSuccessful) {
                   val result = response.body()

                   if (result != null && result.success && result.data != null) {
                       //JWT 토큰 및 만료 시간 저장
                       sharedPreferencesUtil.saveTokens(
                           result.data.accessToken,
                           result.data.accessTokenExpiresIn,
                           result.data.refreshToken,
                           result.data.refreshTokenExpiresIn
                       )
                       // LiveData 업데이트 -> 기본이미지 링크 업로드 필요
                       _joinUser.postValue(JoinUser(result.data.accessToken, result.data.refreshToken, email, ""))

                       //로그인 성공
                       onResult(true, result.data.accessToken)
                   }else {
                       // 실패 시 기본 메시지 또는 errorMessage 사용
                       onResult(false, result?.data?.toString() ?: "알 수 없는 오류 발생")
                   }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    var errorMessage = "로그인 실패"
                    if (!errorBodyString.isNullOrEmpty()) {
                        try {
                            val jsonObject = JSONObject(errorBodyString)
                            val dataObject = jsonObject.getJSONObject("data")
                            errorMessage = dataObject.getString("errorMessage")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    onResult(false, errorMessage)
                }
            }.onFailure { exception ->
                // 로그인 실패면 실패 결과와 에러 메시지 보내기
                onResult(false, exception.message ?: "로그인 중 오류 발생")
            }
        }
    }

    //액세스 토큰 재발급 (`/auth/token/reissue`)
    fun reissueAccessToken(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val refreshToken = sharedPreferencesUtil.getRefreshToken() ?: return@launch onResult(false)

            runCatching {
                RetrofitUtil.authService.reissueAccessToken(RefreshToken(refreshToken))
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        //새 액세스 토큰 저장
                        sharedPreferencesUtil.saveAccessToken(data.accessToken, data.accessTokenExpiresIn)
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                } else {
                    onResult(false)
                }
            }.onFailure {
                onResult(false)
            }
        }
    }

    //리프레시 토큰 재발급 (`/auth/token/renew`)
    fun renewRefreshToken(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val refreshToken = sharedPreferencesUtil.getRefreshToken() ?: return@launch onResult(false)

            runCatching {
                RetrofitUtil.authService.renewRefreshToken(RefreshToken(refreshToken))
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        //새 액세스 & 리프레시 토큰 저장
                        sharedPreferencesUtil.saveTokens(
                            data.accessToken,
                            data.accessTokenExpiresIn,
                            data.refreshToken,
                            data.refreshTokenExpiresIn
                        )
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                } else {
                    onResult(false)
                }
            }.onFailure {
                onResult(false)
            }
        }
    }

}