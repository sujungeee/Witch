package com.ssafy.witch.data.model.dto

data class GroupMember(
    val userId: String,
    val nickname: String,
    val profileImageUrl: String,
    val isGroupLeader: Boolean
)