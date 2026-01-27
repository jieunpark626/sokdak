package com.sokdak.limit.adapter.inbound.api.exceptions

import com.sokdak.common.web.exception.BaseExceptionHandler
import com.sokdak.common.web.exception.ErrorCode
import com.sokdak.common.web.exception.ErrorResponse
import com.sokdak.limit.application.exceptions.LimitExceededException
import com.sokdak.limit.application.exceptions.LimitNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Limit 도메인 전용 예외 처리
 */
@RestControllerAdvice(basePackages = ["com.sokdak.limit"])
@Order(0)
class LimitExceptionHandler : BaseExceptionHandler() {
    @ExceptionHandler(LimitNotFoundException::class)
    fun handleLimitNotFound(
        e: LimitNotFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logWarn("Limit", ErrorCode.LIMIT_NOT_FOUND, e.message, request)
        return buildErrorResponse(ErrorCode.LIMIT_NOT_FOUND, e.message, request)
    }

    @ExceptionHandler(LimitExceededException::class)
    fun handleLimitExceeded(
        e: LimitExceededException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logWarn("Limit", ErrorCode.LIMIT_EXCEEDED, e.message, request)
        return buildErrorResponse(ErrorCode.LIMIT_EXCEEDED, e.message, request)
    }
}
