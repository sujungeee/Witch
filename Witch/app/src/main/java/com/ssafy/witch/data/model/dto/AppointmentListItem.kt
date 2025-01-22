package com.ssafy.witch.data.model.dto

import java.time.LocalDateTime

data class AppointmentListItem(
    val appointmentId: Int,
    val appointmentName: String,
    val appointmentTime: LocalDateTime,
    val appointmentStatus: String
)
