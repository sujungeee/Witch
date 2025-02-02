package com.ssafy.witch.data.model.response

data class AccessTokenResponse(
    val success: Boolean,
    val data: AccessTokenData?
)

data class AccessTokenData(
    val tokenType: String,
    val accessToken: String,
    val accessTokenExpiresIn: Long
)
