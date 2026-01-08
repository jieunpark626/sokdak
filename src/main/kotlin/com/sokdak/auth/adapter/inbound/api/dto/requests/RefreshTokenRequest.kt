package com.sokdak.auth.adapter.inbound.api.dto.requests

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank(message = "Token is required")
    val refreshToken: String,
)
