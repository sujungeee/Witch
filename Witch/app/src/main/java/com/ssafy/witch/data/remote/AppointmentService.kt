package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.AppointmentDetailItem
import com.ssafy.witch.data.model.dto.request.AppointmentRequest
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AppointmentService {

    @GET("/appointments/me")
    suspend fun getMyAppointments(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): BaseResponse<MyAppointmentResponse>

    @GET("/groups/{groupId}/appointments")
    suspend fun getGroupAppointments(
        @Path("groupId") groupId: String,
    ): BaseResponse<MyAppointmentResponse>

    @GET("/appointments/{appointmentId}")
    suspend fun getAppointmentInfo(
        @Path("appointmentId") appointmentId: String
    ): Response<BaseResponse<AppointmentDetailItem>>

    @POST("/groups/{groupId}/appointments")
    suspend fun registerAppointment(
        @Path("groupId") groupId: String,
        @Body appointmentRequest: AppointmentRequest
    ): Response<BaseResponse<Boolean>>

    @POST("/appointments/{appointmentId}/members")
    suspend fun participateAppointment(
        @Path("appointmentId") appointmentId: String
    ): Response<BaseResponse<Boolean>>

    @DELETE("/appointments/{appointmentId}/members/me")
    suspend fun leaveAppointment(
        @Path("appointmentId") appointmentId: String
    ): Response<BaseResponse<Boolean>>

    @DELETE("/appointments/{appointmentId}")
    suspend fun deleteAppointment(
        @Path("appointmentId") appointmentId: String
    ): Response<BaseResponse<Boolean>>
}
