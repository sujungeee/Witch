package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.GroupInfo
import com.ssafy.witch.data.model.dto.Snack
import com.ssafy.witch.data.model.response.GroupListResponse
import com.ssafy.witch.data.model.response.PresignedUrl
import retrofit2.Response
import com.ssafy.witch.data.model.response.SnackResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SnackService {

    @DELETE("/snacks/{snackId}")
    suspend fun deleteSnack(
        @Path("snackId") snackId: String
    ) : Response<BaseResponse<String>>

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


    @GET("/appointment/{appointmentid}/snacks")
    suspend fun getSnackList(
        @Path("appointmentid") appointmentId: String
    ) : Response<BaseResponse<SnackResponse>>

}