package com.sokdak.auth.adapter.inbound.api.exceptions

enum class ErrorCode {
    DUPLICATE_LOGIN_ID,
    DUPLICATE_EMAIL,
    INVALID_PASSWORD,
    USER_NOT_FOUND,
    INVALID_CREDENTIALS,
    INVALID_TOKEN,
    TOKEN_EXPIRED,
}
