package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class MyAppointmentResponse(
    @SerializedName("appointments") val appointments: List<Appointment>
){
    data class Appointment(
        @SerializedName("appointment_id") val appointmentId: String,
        @SerializedName("appointment_name") val appointmentName: String,
        @SerializedName("appointment_time") val appointmentTime: LocalDateTime,
        @SerializedName("apointment_status") val appointmentStatus: String,
        val groupName:String
    )
}

