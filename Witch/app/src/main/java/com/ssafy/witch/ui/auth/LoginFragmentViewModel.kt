package com.ssafy.witch.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.model.dto.Login
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.remote.AuthService
import com.ssafy.witch.data.remote.RetrofitUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "LoginFragmentViewModel"
class LoginFragmentViewModel(application: Application): AndroidViewModel(application) {

//    //뷰모델에서만 쓰는 것
//    private val _joinUser = MutableLiveData<User>()
//    //뷰모델 외 사용 가능한 public 선언
//    val joinUser:LiveData<User>
//        get() = _joinUser

    val sharedPreferencesUtil = SharedPreferencesUtil(application.applicationContext)

    // 이메일, 닉네임, JWT 토큰 로그인 처리
    fun login(email:String, password: String, onResult: (Boolean, String?) -> Unit) {
        //API 호출은 IO 스레드에서 실행
        viewModelScope.launch(Dispatchers.IO) {
            // login 위해 retrofit 으로 가야 함

            val authService = ApplicationClass.retrofit.create(AuthService::class.java)

            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {
                    val fcmToken = it.result ?: ""
                    Log.d(TAG, "login fcmtoken: $fcmToken")

                    // FCM 토큰 발급 완료 후 API 호출
                    viewModelScope.launch {
                        runCatching {
                            authService.login(Login(email, fcmToken, password))
                        }.onSuccess { response ->
                            if (response.success) {
                                response.data?.let { data ->
                                    Log.d(TAG, "서버에서 받은 accessTokenExpiresIn: ${data.accessTokenExpiresIn}")
                                    Log.d(TAG, "서버에서 받은 refreshTokenExpiresIn: ${data.refreshTokenExpiresIn}")

                                    // 저장 전에 로깅
                                    val currentTime = System.currentTimeMillis() / 1000
                                    Log.d(TAG, "현재 시간: $currentTime")
                                    Log.d(TAG, "계산된 AccessToken 만료 시간: ${currentTime + data.accessTokenExpiresIn}")
                                    Log.d(TAG, "계산된 RefreshToken 만료 시간: ${currentTime + data.refreshTokenExpiresIn}")

                                    sharedPreferencesUtil.saveTokens(
                                        data.accessToken,
                                        data.accessTokenExpiresIn,
                                        data.refreshToken,
                                        data.refreshTokenExpiresIn,
                                        data.refreshTokenRenewAvailableSeconds
                                    )
                                    Log.d(TAG, "access: ${data.accessToken}")
                                    Log.d(TAG, "a-expire: ${data.accessTokenExpiresIn}")
                                    Log.d(TAG, "refresh: ${data.refreshToken}")
                                    Log.d(TAG, "r-expire: ${data.refreshTokenExpiresIn}")
                                    Log.d(TAG, "login_avail: ${data.refreshTokenRenewAvailableSeconds}")
                                    onResult(true, data.accessToken)

                                    // LiveData 업데이트 -> 기본이미지 링크 업로드 필요
//                                  _joinUser.postValue(User(response.data.accessToken, response.data.refreshToken, email, ""))

                                } ?: run {
                                    onResult(false, "알 수 없는 오류 발생")
                                }
                            } else {
                                onResult(false, response.error.errorMessage)
                            }
                        }.onFailure { exception ->
                            onResult(false, exception.message ?: "로그인 중 오류 발생")
                        }
                    }
                } else {
                    Log.e(TAG, "Failed to get FCM token", it.exception)
                    onResult(false, "FCM 토큰 발급 실패")
                }
            }
        }
    }

    //액세스 토큰 재발급 (`/auth/token/reissue`)
    fun reissueAccessToken(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val refreshToken = sharedPreferencesUtil.getRefreshToken() ?: return@launch onResult(false)

            Log.d(TAG, "reissueAccessToken_refreshToken: $refreshToken")

            val authService = ApplicationClass.retrofit.create(AuthService::class.java)
            runCatching {
                authService.reissueAccessToken(RefreshToken(refreshToken))
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

            Log.d(TAG, "renewRefreshToken_refreshToken: $refreshToken")
            val authService = ApplicationClass.retrofit.create(AuthService::class.java)
            runCatching {
                authService.renewRefreshToken(RefreshToken(refreshToken))
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        //새 액세스 & 리프레시 토큰 저장
                        sharedPreferencesUtil.saveTokens(
                            data.accessToken,
                            data.accessTokenExpiresIn,
                            data.refreshToken,
                            data.refreshTokenExpiresIn,
                            data.refreshTokenRenewAvailableSeconds
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