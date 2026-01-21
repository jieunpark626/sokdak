package com.sokdak.common.web.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String,
) {
    // Common
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),

    // Auth
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "Login ID already exists"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "Email already exists"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid password format"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token expired"),
    EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, "Email not verified"),
    VERIFICATION_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "Verification token not found"),
    VERIFICATION_TOKEN_EXPIRED(HttpStatus.GONE, "Verification token expired"),
    EMAIL_ALREADY_VERIFIED(HttpStatus.CONFLICT, "Email already verified"),
    EMAIL_SEND_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "Failed to send email"),

    // Journal
    JOURNAL_NOT_FOUND(HttpStatus.NOT_FOUND, "Journal not found"),

    // Limit
    LIMIT_NOT_FOUND(HttpStatus.NOT_FOUND, "Limit not found"),
    LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "Daily limit exceeded"),
}
