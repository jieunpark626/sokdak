package com.sokdak.auth.domain.valueobjects

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresInSeconds: Long,
    val refreshTokenExpiresInSeconds: Long,
)
