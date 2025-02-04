package com.ssafy.witch.Auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.Join
import com.ssafy.witch.data.remote.RetrofitUtil
import kotlinx.coroutines.launch
import retrofit2.Response


class JoinFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val AuthService = RetrofitUtil.authService

    //이메일 중복 확인
    fun checkEmailUnique(email: String, onResult: (Boolean, String?) -> Unit) {
        executeRequest({ AuthService.checkEmailUnique(email) }, onResult)
    }

    //닉네임 중복 확인
    fun checkNicknameUnique(nickname: String, onResult: (Boolean, String?) -> Unit) {
        executeRequest({ AuthService.checkNicknameUnique(nickname) }, onResult)
    }

    //이메일 인증 코드 요청
    fun requestEmailVerification(email: String, onResult: (Boolean, String?) -> Unit) {
        executeRequest({ AuthService.requestEmailVerification(mapOf("email" to email)) }, onResult)
    }

    //이메일 인증 코드 확인
    fun confirmEmailVerification(email: String, code: String, onResult: (Boolean, String?) -> Unit) {
        executeRequest({ AuthService.confirmEmailVerification(mapOf("email" to email, "emailVerificationCode" to code)) }, onResult)
    }

    //회원가입 요청
    fun registerUser(request: Join, onResult: (Boolean, String?) -> Unit) {
        executeRequest({ AuthService.registerUser(request) }, onResult)
    }

    //공통 API 요청 처리 함수
    private fun <T> executeRequest(
        request: suspend () -> Response<BaseResponse<T>>,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = request()
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result?.success == true) {
                        onResult(true, null)
                    } else {
                        onResult(false, result?.data?.toString() ?: "알 수 없는 오류 발생")
                    }
                } else {
                    onResult(false, response.errorBody()?.string() ?: "네트워크 오류")
                }
            } catch (e: Exception) {
                onResult(false, e.message ?: "예기치 않은 오류 발생")
            }
        }
    }
}