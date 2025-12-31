package com.sokdak.auth.domain.valueobjects

@JvmInline
value class LoginId(val value: String) {
    init {
        require(value.length >= 4) {
            "Login ID must be at least 4 characters"
        }
    }
}
