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
import com.ssafy.witch.data.remote.RetrofitUtil.Companion.tokenService
import com.ssafy.witch.ui.LoginActivity
import com.ssafy.witch.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

private const val TAG = "TokenAuthenticator"
class TokenAuthenticator(
    private val sharedPreferencesUtil: SharedPreferencesUtil,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (getRetryCount(response) >= MAX_RETRY_COUNT) {
            forceLogout()
            return null
        }

        Log.d(TAG, "🚨 401 발생 → 요청 URL: ${response.request.url}")

        // 마지막으로 한 번 더 시도
        val isOk = TokenManager.ensureValidToken()
        return if (isOk) {
            // 새로 갱신된 AccessToken 얻어와 재시도
            val newAccess = sharedPreferencesUtil.getAccessToken() ?: ""
            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccess")
                .build()
        } else {
            forceLogout()
            null
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
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "로그인 정보가 만료되었습니다.\n" +
                    " 다시 로그인 해주세요.", Toast.LENGTH_LONG).show()
        }

        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    companion object {
        private const val MAX_RETRY_COUNT = 3
    }
}
