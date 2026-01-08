package com.sokdak.auth.adapter.inbound.api.exceptions

import com.sokdak.auth.adapter.inbound.api.dto.responses.ApiErrorResponse
import com.sokdak.auth.application.exceptions.AuthException
import com.sokdak.auth.application.exceptions.DuplicateEmailException
import com.sokdak.auth.application.exceptions.DuplicateLoginIdException
import com.sokdak.auth.application.exceptions.EmailAlreadyVerifiedException
import com.sokdak.auth.application.exceptions.EmailNotVerifiedException
import com.sokdak.auth.application.exceptions.EmailSendFailedException
import com.sokdak.auth.application.exceptions.InvalidCredentialsException
import com.sokdak.auth.application.exceptions.InvalidPasswordException
import com.sokdak.auth.application.exceptions.InvalidTokenException
import com.sokdak.auth.application.exceptions.TokenExpiredException
import com.sokdak.auth.application.exceptions.UserNotFoundException
import com.sokdak.auth.application.exceptions.VerificationTokenExpiredException
import com.sokdak.auth.application.exceptions.VerificationTokenNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.sokdak.auth"])
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

                is InvalidCredentialsException ->
                    HttpStatus.UNAUTHORIZED to ErrorCode.INVALID_CREDENTIALS

                is InvalidTokenException ->
                    HttpStatus.UNAUTHORIZED to ErrorCode.INVALID_TOKEN

                is TokenExpiredException ->
                    HttpStatus.UNAUTHORIZED to ErrorCode.TOKEN_EXPIRED

                is EmailNotVerifiedException ->
                    HttpStatus.FORBIDDEN to ErrorCode.EMAIL_NOT_VERIFIED

                is VerificationTokenNotFoundException ->
                    HttpStatus.NOT_FOUND to ErrorCode.VERIFICATION_TOKEN_NOT_FOUND

                is VerificationTokenExpiredException ->
                    HttpStatus.GONE to ErrorCode.VERIFICATION_TOKEN_EXPIRED

                is EmailAlreadyVerifiedException ->
                    HttpStatus.CONFLICT to ErrorCode.EMAIL_ALREADY_VERIFIED

                is EmailSendFailedException ->
                    HttpStatus.SERVICE_UNAVAILABLE to ErrorCode.EMAIL_SEND_FAILED
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
