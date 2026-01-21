package com.sokdak.auth.adapter.inbound.api.exceptions

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
import com.sokdak.common.web.exception.BaseExceptionHandler
import com.sokdak.common.web.exception.ErrorCode
import com.sokdak.common.web.exception.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Auth 도메인 전용 예외 처리
 */
@RestControllerAdvice(basePackages = ["com.sokdak.auth"])
class AuthExceptionHandler : BaseExceptionHandler() {
    @ExceptionHandler(AuthException::class)
    fun handleAuthException(
        e: AuthException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val errorCode =
            when (e) {
                is DuplicateLoginIdException -> ErrorCode.DUPLICATE_LOGIN_ID
                is DuplicateEmailException -> ErrorCode.DUPLICATE_EMAIL
                is InvalidPasswordException -> ErrorCode.INVALID_PASSWORD
                is UserNotFoundException -> ErrorCode.USER_NOT_FOUND
                is InvalidCredentialsException -> ErrorCode.INVALID_CREDENTIALS
                is InvalidTokenException -> ErrorCode.INVALID_TOKEN
                is TokenExpiredException -> ErrorCode.TOKEN_EXPIRED
                is EmailNotVerifiedException -> ErrorCode.EMAIL_NOT_VERIFIED
                is VerificationTokenNotFoundException -> ErrorCode.VERIFICATION_TOKEN_NOT_FOUND
                is VerificationTokenExpiredException -> ErrorCode.VERIFICATION_TOKEN_EXPIRED
                is EmailAlreadyVerifiedException -> ErrorCode.EMAIL_ALREADY_VERIFIED
                is EmailSendFailedException -> ErrorCode.EMAIL_SEND_FAILED
            }

        logWarn("Auth", errorCode, e.message, request)
        return buildErrorResponse(errorCode, e.message, request)
    }
}
