package com.ssafy.witch.data.model.dto

import java.time.LocalDateTime

data class User(
    val userId: String,
    val email: String,
    val nickname: String,
//    val password: String, // 서버에서 관리하면 필요 없는 필드 값
    val profileImage: String, // 프로필 이미지, Pre-Signed URL
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val modifiedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor():this("", "", "","")
    constructor(email:String, nickname: String):this("", email, nickname, "")
}