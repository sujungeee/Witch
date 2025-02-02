package com.ssafy.witch.data.model.response

data class AuthResponse(
    val success: Boolean,
    val data: LoginData? = null,
    val error: ErrorResponse? = null
)

data class LoginData(
    val tokenType: String,   // ex) "Bearer"
    val accessToken: String,   // ex) "access.token.example"
    val accessTokenExpiresIn: Long,  // ex) 3600 (초 단위)
    val refreshToken: String,   // ex) "refresh.token.example"
    val refreshTokenExpiresIn: Long,  // ex) 36000 (초 단위)
    val refreshTokenRenewAvailableSeconds: Long  // ex) 3600 (초 단위)
)



