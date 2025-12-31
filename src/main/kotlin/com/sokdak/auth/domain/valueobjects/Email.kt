package com.sokdak.auth.domain.valueobjects

@JvmInline
value class Email(val value: String) {
    init {
        require(value.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))) {
            "Invalid email format"
        }
    }
}
