package com.sokdak.auth.application.commands

data class VerifyEmailCommand(
    val token: String,
)
