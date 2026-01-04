package com.sokdak.auth.adapter.inbound.api.exceptions

import com.sokdak.auth.adapter.inbound.api.dto.responses.ApiErrorResponse
import com.sokdak.auth.application.exceptions.AuthException
import com.sokdak.auth.application.exceptions.DuplicateEmailException
import com.sokdak.auth.application.exceptions.DuplicateLoginIdException
import com.sokdak.auth.application.exceptions.InvalidPasswordException
import com.sokdak.auth.application.exceptions.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandler {
    @ExceptionHandler(AuthException::class)
    fun handleAuthException(e: AuthException): ResponseEntity<ApiErrorResponse> {
        val (status, errorCode) =
            when (e) {
                is DuplicateLoginIdException ->
                    HttpStatus.CONFLICT to ErrorCode.DUPLICATE_LOGIN_ID

                is DuplicateEmailException ->
                    HttpStatus.CONFLICT to ErrorCode.DUPLICATE_EMAIL

                is InvalidPasswordException ->
                    HttpStatus.BAD_REQUEST to ErrorCode.INVALID_PASSWORD

                is UserNotFoundException ->
                    HttpStatus.NOT_FOUND to ErrorCode.USER_NOT_FOUND
            }

        return ResponseEntity
            .status(status)
            .body(
                ApiErrorResponse(
                    code = errorCode.name,
                    message = e.message ?: "Auth error",
                ),
            )
    }
}
