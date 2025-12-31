package com.sokdak.auth.domain.valueobjects

@JvmInline
value class HashedPassword(val value: String) {
    init {
        require(value.isNotBlank()) { "Hashed password cannot be blank" }
    }
}