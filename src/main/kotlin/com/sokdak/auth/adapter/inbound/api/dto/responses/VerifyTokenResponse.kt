package com.sokdak.auth.adapter.inbound.api.dto.responses

data class VerifyTokenResponse(
    val valid: Boolean,
    val userId: String?,
)
