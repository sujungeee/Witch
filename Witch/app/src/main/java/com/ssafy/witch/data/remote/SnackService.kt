package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.GroupInfo
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.data.model.response.PresignedUrl
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface SnackService {

    @POST("/snacks/snack-image/presigned-url")
    suspend fun getImagePresignedUrl(
        @Body filename: String
    ) : BaseResponse<PresignedUrl>

    @POST("/snacks/snack-audio/presigned-url")
    suspend fun getAudioPresignedUrl(
        @Body filename: String
    ) : BaseResponse<PresignedUrl>

    @POST("/snacks")
    suspend fun createSnack(
//        @Body snackInfo: SnackInfo,
    ) : BaseResponse<String>

}