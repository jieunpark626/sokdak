package com.sokdak.limit.adapter.inbound.api.exceptions

import com.sokdak.limit.adapter.inbound.api.dto.responses.ErrorResponse
import com.sokdak.limit.application.exceptions.LimitExceededException
import com.sokdak.limit.application.exceptions.LimitNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.sokdak.limit"])
class LimitExceptionHandler {
    private val logger = LoggerFactory.getLogger(LimitExceptionHandler::class.java)

    @ExceptionHandler(LimitNotFoundException::class)
    fun handleLimitNotFound(e: LimitNotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    code = ErrorCode.LIMIT_NOT_FOUND.name,
                    message = e.message ?: "Limit not found",
                ),
            )

    @ExceptionHandler(LimitExceededException::class)
    fun handleLimitExceeded(e: LimitExceededException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body(
                ErrorResponse(
                    code = ErrorCode.LIMIT_EXCEEDED.name,
                    message = e.message ?: "Daily limit exceeded",
                ),
            )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message =
            e.bindingResult.fieldErrors
                .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    code = ErrorCode.INVALID_REQUEST.name,
                    message = message,
                ),
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected error occurred", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    code = ErrorCode.INTERNAL_SERVER_ERROR.name,
                    message = e.message ?: "Internal server error",
                ),
            )
    }
}
