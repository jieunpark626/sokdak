package com.sokdak.auth.application.commands

data class LoginCommand(
    val loginId: String,
    val password: String,
)
