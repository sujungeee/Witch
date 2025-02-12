package com.ssafy.witch.data.remote.network

import android.util.Log
import com.ssafy.witch.base.ApplicationClass
import com.ssafy.witch.data.local.SharedPreferencesUtil
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.model.response.AccessTokenResponse
import com.ssafy.witch.data.remote.AuthService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

class TokenAuthenticator(
    private val sharedPreferencesUtil: SharedPreferencesUtil
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val currentToken = sharedPreferencesUtil.getAccessToken() // jwt 토큰
        if (response.request.header("Authorization") == "Bearer $currentToken") {
            val refreshToken = sharedPreferencesUtil.getRefreshToken() ?: return null

            val newTokenResponse = runBlocking {
                reissueAccessToken(refreshToken)
            }
            return if (newTokenResponse != null) {
                sharedPreferencesUtil.saveAccessToken(newTokenResponse.accessToken)
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokenResponse.accessToken}")
                    .build()
            } else {
                null
            }
        }
        return null
    }

    private suspend fun reissueAccessToken(refreshToken: String): AccessTokenResponse? {
        return try {
            val bearerToken = "Bearer $refreshToken" // ✅ Bearer 추가
            val response = ApplicationClass.retrofit.create(AuthService::class.java)
                .reissueAccessToken(RefreshToken(bearerToken)) // ✅ Bearer 포함된 토큰 전송

            if (response.isSuccessful) {
                response.body()?.also {
                    Log.d("TokenAuthenticator", "새로운 AccessToken: ${it.accessToken}")
                }
            } else {
                Log.e("TokenAuthenticator", "토큰 재발급 실패: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("TokenAuthenticator", "네트워크 오류 발생", e)
            null
        }
    }

}