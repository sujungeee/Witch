package com.ssafy.witch.data.remote.network

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.model.response.ErrorResponse
import com.ssafy.witch.data.remote.AuthService
import com.ssafy.witch.ui.LoginActivity
import com.ssafy.witch.ui.MainActivity
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val sharedPreferencesUtil: SharedPreferencesUtil,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 무한 재시도를 방지 (최대 3회)
        if (getRetryCount(response) >= MAX_RETRY_COUNT) return null

        val currentTime = System.currentTimeMillis() / 1000
        val storedAccessToken = sharedPreferencesUtil.getAccessToken()
        val storedRefreshToken = sharedPreferencesUtil.getRefreshToken()
        val accessTokenExpiresAt = sharedPreferencesUtil.getAccessTokenExpiresAt()
        val refreshTokenExpiresAt = sharedPreferencesUtil.getRefreshTokenExpiresAt()
        val refreshTokenIssuedAt = sharedPreferencesUtil.getRefreshTokenRenewAvailableSeconds()

        // 리프레쉬토큰 시간 초과시 강제 로그아웃 실행
        // 초기값 자동 로그인 방지
        if(refreshTokenExpiresAt < currentTime) {
            Log.e(TAG, "❌ Refresh Token 시간 만료 → 로그인 필요")
            forceLogout()
            return null
        }

        if (getRetryCount(response) >= 2) {
            Log.e(TAG, "🚨 이미 토큰 재발급 요청을 시도했음 → 강제 로그아웃")
            forceLogout()
            return null
        }

        // 저장된 Refresh Token 확인
        if (storedRefreshToken.isNullOrEmpty()) {
            Log.e(TAG, "❌ 저장된 Refresh Token 없음 → 로그인 필요")
            forceLogout()
            return null
        }

        // 동기적으로 새 Access Token 재발급 요청, 액세스 토큰 재갱신은 중앙집중으로 처리
        val accessTokenResponseBaseResponse = runBlocking {
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
        if (accessTokenResponseBaseResponse != null && accessTokenResponseBaseResponse.success && accessTokenResponseBaseResponse.data?.accessToken != null) {
            val newAccessToken = accessTokenResponseBaseResponse.data.accessToken
            val expiresIn = accessTokenResponseBaseResponse.data.accessTokenExpiresIn

            sharedPreferencesUtil.saveAccessToken(newAccessToken, expiresIn)
            Log.d(TAG, "✅ 새 Access Token 발급 성공: $newAccessToken")

            return response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        } else {
            // error가 null인 경우, 내쪽에서 기본 ErrorResponse를 생성해서 사용
            val errorResponse = accessTokenResponseBaseResponse?.error ?: ErrorResponse(
                errorCode = "NO_ERROR_CODE",
                errorMessage = "No error details provided."
            )
            Log.e(TAG, "❌ Access Token 갱신 실패 → 로그인 필요, error: $errorResponse")
//            forceLogout()
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

        // ✅ UI 스레드에서 실행하여 Toast 표시
//        Handler(Looper.getMainLooper()).post {
//            Toast.makeText(context, "세션이 만료되었습니다. 다시 로그인하세요.", Toast.LENGTH_LONG).show()
//        }

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
