package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.Login
import com.ssafy.witch.data.model.dto.RefreshToken
import com.ssafy.witch.data.model.response.AccessTokenResponse
import com.ssafy.witch.data.model.response.LoginResponse
import com.ssafy.witch.data.model.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {
    //로그인 처리 후 성공적으로 로그인 되었다면 JWT 토큰 발급 API
    //서버에서 정확히 “Content-Type: application/json”만을 요구, 메서드 단위의 @Headers 어노테이션을 사용
    @POST("auth/login")
    suspend fun login(@Body request: Login): BaseResponse<LoginResponse>

    //액세스 토큰 재발급 API
    @POST("auth/token/reissue")
    suspend fun reissueAccessToken(@Body request: RefreshToken): Response<AccessTokenResponse>

    //리프레시 토큰 재발급 API
    @POST("auth/token/renew")
    suspend fun renewRefreshToken(@Body request: RefreshToken): Response<RefreshTokenResponse>
}