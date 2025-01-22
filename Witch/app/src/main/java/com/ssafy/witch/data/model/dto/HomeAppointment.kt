package com.ssafy.witch.data.model.dto

import java.time.LocalDateTime

data class HomeAppointment (
    var appointment_id: Int,
    var group_name: String,
    var appointment_time: LocalDateTime,
    var appointment_name: String
)
