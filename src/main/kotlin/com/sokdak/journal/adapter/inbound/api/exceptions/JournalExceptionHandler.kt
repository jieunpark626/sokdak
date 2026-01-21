package com.sokdak.journal.adapter.inbound.api.exceptions

import com.sokdak.common.web.exception.BaseExceptionHandler
import com.sokdak.common.web.exception.ErrorCode
import com.sokdak.common.web.exception.ErrorResponse
import com.sokdak.journal.application.exceptions.JournalNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Journal 도메인 전용 예외 처리
 */
@RestControllerAdvice(basePackages = ["com.sokdak.journal"])
class JournalExceptionHandler : BaseExceptionHandler() {
    @ExceptionHandler(JournalNotFoundException::class)
    fun handleJournalNotFound(
        e: JournalNotFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logWarn("Journal", ErrorCode.JOURNAL_NOT_FOUND, e.message, request)
        return buildErrorResponse(ErrorCode.JOURNAL_NOT_FOUND, e.message, request)
    }
}
