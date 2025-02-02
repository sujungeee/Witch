package com.ssafy.witch.data.remote

import com.ssafy.witch.data.model.response.CommonResponse
import okhttp3.Response
import retrofit2.http.GET

interface AppointmentService {

    @GET("/appointments/me")
    suspend fun getMyAppointments(): CommonResponse

}