package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.GroupInfo
import com.ssafy.witch.data.model.dto.Snack
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.data.model.response.PresignedUrl
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface SnackService {

    @POST("/snacks/snack-image/presigned-url")
    suspend fun getImagePresignedUrl(
        @Body filename: String
    ) : Response<BaseResponse<PresignedUrl>>

    @POST("/snacks/snack-sound/presigned-url")
    suspend fun getAudioPresignedUrl(
        @Body filename: String
    ) : Response<BaseResponse<PresignedUrl>>

    @POST("/appointments/{appointmentId}/snacks")
    suspend fun createSnack(
        @Path("appointmentId") appointmentId: String,
        @Body snack: Snack,
    ) : Response<BaseResponse<String>>


    @GET("/snacks/{appointmentId}")
    suspend fun getSnack(
        @Path("appointmentId") appointmentId: String
    ) : Response<BaseResponse<Snack>>


    @GET("/snacks/{appointmentId}")
    suspend fun getSnackList(
        @Path("appointmentId") appointmentId: String
    ) : Response<BaseResponse<List<Snack>>>

}