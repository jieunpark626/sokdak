package com.sokdak.auth.adapter.inbound.api.dto.responses

data class UserResponse(
    val id: String,
    val loginId: String,
    val email: String,
    val name: String,
    val gender: String,
    val plan: String,
)
