package com.ssafy.witch.data.remote.network

import android.content.Intent
import android.util.Log
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.remote.AuthService
import com.ssafy.witch.ui.LoginActivity
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 무한 재시도를 방지 (최대 3회)
        if (getRetryCount(response) >= MAX_RETRY_COUNT) return null

        // 저장된 Refresh Token 확인
        val storedRefreshToken = sharedPreferencesUtil.getRefreshToken()
        if (storedRefreshToken.isNullOrEmpty()) {
            Log.e(TAG, "❌ 저장된 Refresh Token 없음 → 로그인 필요")
            forceLogout()
            return null
        }

        // 동기적으로 새 Access Token 재발급 요청
        val newTokenResponse = runBlocking {
            try {
                val authService = ApplicationClass.retrofit.create(AuthService::class.java)
                val tokenResponse = authService.reissueAccessToken(RefreshToken("Bearer $storedRefreshToken"))
                if (tokenResponse.isSuccessful) tokenResponse.body() else null
            } catch (e: Exception) {
                Log.e(TAG, "❌ Access Token 갱신 중 오류 발생", e)
                null
            }
        }

        // 재발급 성공 여부를 체크할 때 error 객체가 null인 경우 기본 값을 할당
        if (newTokenResponse != null && newTokenResponse.success && newTokenResponse.data?.accessToken != null) {
            val newAccessToken = newTokenResponse.data.accessToken
            val expiresIn = newTokenResponse.data.accessTokenExpiresIn

            sharedPreferencesUtil.saveAccessToken(newAccessToken, expiresIn)
            Log.d(TAG, "✅ 새 Access Token 발급 성공: $newAccessToken")

            return response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        } else {
            // error가 null인 경우, 내쪽에서 기본 ErrorResponse를 생성해서 사용
            val errorResponse = newTokenResponse?.error ?: ErrorResponse(
                errorCode = "NO_ERROR_CODE",
                errorMessage = "No error details provided."
            )
            Log.e(TAG, "❌ Access Token 갱신 실패 → 로그인 필요, error: $errorResponse")
            forceLogout()
            return null
        }
    }

    /**
     * 이전에 재시도한 횟수를 반환합니다.
     */
    private fun getRetryCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }
        return count
    }

    /**
     * Refresh Token이 없거나 재발급 실패 시 로그아웃 처리합니다.
     */
    private fun forceLogout() {
        Log.e(TAG, "🚨 강제 로그아웃 처리: 저장된 토큰 모두 삭제")
        sharedPreferencesUtil.clearToken()

        val context = ApplicationClass.instance.applicationContext
        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    companion object {
        private const val TAG = "TokenAuthenticator"
        private const val MAX_RETRY_COUNT = 3
    }
}
