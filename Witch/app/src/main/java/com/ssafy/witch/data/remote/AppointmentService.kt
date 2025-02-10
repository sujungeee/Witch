package com.ssafy.witch.data.remote

import com.ssafy.witch.base.BaseResponse
import com.ssafy.witch.data.model.response.MyAppointmentResponse
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AppointmentService {

    @GET("/appointments/me")
    suspend fun getMyAppointments(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): BaseResponse<MyAppointmentResponse>

}