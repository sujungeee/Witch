package com.ssafy.witch.data.model.response

import com.ssafy.witch.data.model.dto.User

data class GroupApproval (
    val joinRequestId: String,
    val user: User
)