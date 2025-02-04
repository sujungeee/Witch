package com.ssafy.witch.data.remote

import com.ssafy.witch.data.model.dto.Login
import com.ssafy.witch.data.model.response.CommonResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {

    @PATCH("users/me/nickname")
    suspend fun editProfileName(
        @Body name: String
    ) : CommonResponse


    @POST("users/me/profile-image/presigned-url")
    suspend fun getPresignedUrl(
        @Body filename: String
    ) : CommonResponse


    @PATCH("users/me/profile-image")
    suspend fun editProfileImage(
        @Body objectKey: String
    ) : CommonResponse
}