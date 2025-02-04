package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.Join
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface JoinService {

    // 서버 오류 -parameter 발생
    //이메일 중복 검증 (GET 요청, URL 파라미터)
    @GET("users/email/is-unique")
    suspend fun checkEmailUnique(@Query("email") email: String): Response<BaseResponse<Boolean>>

    // 서버 사용자 인증 에러 발생
    //닉네임 중복 검증 (GET 요청, URL 파라미터)
    @GET("users/nickname/is-unique")
    suspend fun checkNicknameUnique(@Query("nickname") nickname: String): Response<BaseResponse<Boolean>>

    //이메일 인증 코드 발급 요청 (POST 요청, 이메일을 Body로 전달)
    @Headers("Content-Type: application/json")
    @POST("users/email-verification-code")
    suspend fun requestEmailVerification(@Body emailRequest: Map<String, String>): Response<BaseResponse<Boolean>>

    //이메일 인증 코드 확인 (POST 요청, 이메일 & 인증 코드 전달)
    @Headers("Content-Type: application/json")
    @POST("users/email-verification-code/confirm")
    suspend fun confirmEmailVerification(@Body verificationRequest: Map<String, String>): Response<BaseResponse<Boolean>>

    //회원가입 (POST 요청, 전체 회원 정보 전달)
    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun registerUser(@Body request: Join): Response<BaseResponse<Boolean>>


}