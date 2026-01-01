package com.sokdak.auth.adapter.inbound.api.dto.responses

data class LoginResponse(
    val user: LoginUserResponse,
    val token: TokenResponse,
)

data class LoginUserResponse(
    val id: String,
    val name: String,
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresInSeconds: Long,
)
