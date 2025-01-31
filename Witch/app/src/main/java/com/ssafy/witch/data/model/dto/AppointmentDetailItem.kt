package com.ssafy.witch.data.model.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class AppointmentDetailItem(
    var appointment_id: Int,
    var appointment_name: String,
    var appointment_summary: String,
    var appointment_time: LocalDateTime,
    var appointment_address: String,
    var latitude: BigDecimal,
    var longitude: BigDecimal,
    var participants_counts: Int,
    var is_user_leader: Boolean,
    var status: String
) {
    data class Participants (
        var user_id:Int,
        var nickname:String,
        var profile_image_url:String,
        var is_leader: Boolean
    )
}