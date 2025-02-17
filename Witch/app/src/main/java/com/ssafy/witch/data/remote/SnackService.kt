package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.GroupInfo
import com.ssafy.witch.data.model.dto.SnackItem
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.data.model.response.PresignedUrl
import com.ssafy.witch.data.model.response.SnackResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("/appointment/{appointmentid}/snacks")
    suspend fun getSnackList(
        @Path("appointmentid") appointmentId: String
    ) : Response<BaseResponse<SnackResponse>>

}