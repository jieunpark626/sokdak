package com.sokdak.common.web.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    // 1. VO나 비즈니스 로직에서 던지는 IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<ErrorBody> {
        return ResponseEntity.badRequest()
            .body(ErrorBody(message = e.message ?: "Invalid input"))
    }

    // 2. DTO의 @NotBlank 등 검증 실패 시 발생하는 에러 처리
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<ErrorBody> {
        val message = e.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Validation failed"
        return ResponseEntity.badRequest()
            .body(ErrorBody(message = message))
    }

    data class ErrorBody(val message: String)
}
