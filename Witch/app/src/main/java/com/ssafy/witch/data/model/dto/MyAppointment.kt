package com.ssafy.witch.data.model.dto

import java.time.LocalDateTime

data class MyAppointment(
    val appointmentId: String,
    val appointmentTime: String,
    val group: Group,
    val name: String,
    val status: String
){
    data class Group(
        val groupId: String,
        val groupImageUrl: String,
        val name: String
    )
}