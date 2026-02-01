package com.sokdak.auth.adapter.inbound.api.dto.responses

data class TokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresInSeconds: Long,
)
