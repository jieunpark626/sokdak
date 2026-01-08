package com.sokdak.auth.application.dto

data class VerifyTokenResult(
    val valid: Boolean,
    val userId: String?,
)
