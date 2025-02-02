package com.ssafy.witch.data.model.response

data class RefreshTokenResponse(
    val success: Boolean,
    val data: RefreshTokenData?
)

data class RefreshTokenData(
    val tokenType: String,
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val refreshToken: String,
    val refreshTokenExpiresIn: Long,
    val refreshTokenRenewAvailableSeconds: Long
)
