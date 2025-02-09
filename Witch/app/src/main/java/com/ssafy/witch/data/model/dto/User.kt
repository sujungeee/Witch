package com.ssafy.witch.data.model.dto

data class User (
    val userId: String,
    val email: String,
    val nickname: String,
    val profileImageUrl: String
){
    constructor():this("", "", "","")
}