package com.ssafy.witch.data.model.response

import com.google.gson.annotations.SerializedName
import com.ssafy.witch.data.model.dto.User

data class GroupApproval (
    @SerializedName("joinRequestId") val joinRequestId: String,
    @SerializedName("User") val user: User,
)