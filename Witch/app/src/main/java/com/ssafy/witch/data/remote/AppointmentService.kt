package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.dto.request.AppointmentRequest
import com.ssafy.witch.data.model.response.AppointmentDetailResponse
import com.ssafy.witch.data.model.response.MyAppointmentResponse
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
    suspend fun getGroupAppointmentList(
    ): BaseResponse<MyAppointmentResponse>

    @GET("/appointments/{appointmentId}")
    suspend fun getAppointmentDetail(
    ): BaseResponse<AppointmentDetailResponse>

    @POST("/groups/{groupId}/appointments")
    suspend fun registerAppointment(
        @Path("groupId") groupId: Int,
        @Body request: AppointmentRequest
    ): BaseResponse<Boolean>

    @POST("/appointmments/{appointmentId}/members")
    suspend fun participateAppointment(
        @Path("appointmentId") appointmentId: Int
    ): BaseResponse<Boolean>

    @POST("/appointments/{appointmentId}/leave")
    suspend fun leaveAppointment(
        @Path("appointmentId") appointmentId: Int
    ): BaseResponse<Boolean>

    @DELETE("/appointments/{appointmentId}/delete")
    suspend fun deleteAppointment(
        @Path("appointmentId") appointmentId: Int
    ): BaseResponse<Boolean>
}