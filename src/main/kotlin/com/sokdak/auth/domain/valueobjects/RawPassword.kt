package com.sokdak.auth.domain.valueobjects

@JvmInline
value class RawPassword private constructor(val value: String) {
    companion object {
        fun of(password: String): RawPassword { // of?
            require(password.length >= 8) {
                "Password must be at least 8 characters"
            }
            require(password.any { it.isUpperCase() }) {
                "Password must contain at least one uppercase letter"
            }
            require(password.any { it.isLowerCase() }) {
                "Password must contain at least one lowercase letter"
            }
            require(password.any { it.isDigit() }) {
                "Password must contain at least one digit"
            }
            return RawPassword(password)
        }
    }
}
