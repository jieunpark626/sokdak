package com.sokdak.auth.application.dto

data class AuthTokenDto(
    val accessToken: String,
    val refreshToken: String,
    val expiresInSeconds: Long,
)
