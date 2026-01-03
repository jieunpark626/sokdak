package com.sokdak.auth.adapter.inbound.api.dto.responses

data class LoginResponse(
    val user: LoginUserResponse,
    val tokens: TokenResponse,
)



