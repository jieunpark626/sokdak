package com.sokdak.journal.adapter.inbound.api.exceptions

import com.sokdak.journal.adapter.inbound.api.dto.responses.ErrorResponse
import com.sokdak.journal.application.exceptions.JournalNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.sokdak.journal"])
class JournalExceptionHandler {
    // 404 - 일기 없음
    @ExceptionHandler(JournalNotFoundException::class)
    fun handleJournalNotFound(e: JournalNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    code = ErrorCode.JOURNAL_NOT_FOUND.name,
                    message = e.message ?: "Journal not found",
                ),
            )
    }

    // 400 - Validation 에러 (@Valid)
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

    // 500 - 나머지 전부
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
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
