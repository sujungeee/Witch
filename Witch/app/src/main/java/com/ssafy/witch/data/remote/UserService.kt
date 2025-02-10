package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.Login
import com.ssafy.witch.data.model.dto.User
import com.ssafy.witch.data.model.response.PresignedUrl
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {

    @GET("users/me")
    suspend fun getProfile() : BaseResponse<User>

    @PATCH("users/me/nickname")
    suspend fun editProfileName(
        @Body name: String
    ) : BaseResponse<String>


    @POST("users/me/profile-image/presigned-url")
    suspend fun getPresignedUrl(
        @Body filename: String
    ) : BaseResponse<PresignedUrl>


    @PATCH("users/me/profile-image")
    suspend fun editProfileImage(
        @Body objectKey: String
    ) : BaseResponse<String>
}