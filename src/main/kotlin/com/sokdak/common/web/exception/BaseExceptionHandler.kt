package com.sokdak.common.web.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * 모든 도메인별 ExceptionHandler가 상속받는 추상 클래스
 *
 * 공통 기능:
 * - 에러 응답 빌드 (buildErrorResponse)
 * - 로깅 유틸 (logWarn, logError)
 * - 공통 예외 처리 (Validation, IllegalArgument, 500)
 */
abstract class BaseExceptionHandler {
    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    // ==================== 공통 예외 처리 ====================

    /**
     * Validation 에러 (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val message =
            e.bindingResult.fieldErrors
                .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        logWarn("Validation", ErrorCode.INVALID_REQUEST, message, request)
        return buildErrorResponse(ErrorCode.INVALID_REQUEST, message, request)
    }

    /**
     * IllegalArgumentException (VO, 비즈니스 로직 검증)
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        e: IllegalArgumentException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logWarn("IllegalArgument", ErrorCode.INVALID_REQUEST, e.message, request)
        return buildErrorResponse(ErrorCode.INVALID_REQUEST, e.message, request)
    }

    /**
     * 500 - 나머지 모든 예외 (반드시 스택트레이스 로깅!)
     */
    @ExceptionHandler(Exception::class)
    fun handleException(
        e: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        logError("InternalServerError", e, request)
        return buildErrorResponse(
            ErrorCode.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred",
            request,
        )
    }

    // ==================== 유틸리티 메서드 ====================

    /**
     * 에러 응답 빌드
     */
    protected fun buildErrorResponse(
        errorCode: ErrorCode,
        message: String?,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(errorCode.status)
            .body(
                ErrorResponse(
                    status = errorCode.status.value(),
                    code = errorCode.name,
                    message = message ?: errorCode.message,
                    path = request.requestURI,
                ),
            )

    /**
     * WARN 레벨 로깅 (4xx 에러용)
     */
    protected fun logWarn(
        tag: String,
        errorCode: ErrorCode,
        message: String?,
        request: HttpServletRequest,
    ) {
        log.warn(
            "[{}] code={}, message={}, path={}",
            tag,
            errorCode.name,
            message,
            request.requestURI,
        )
    }

    /**
     * ERROR 레벨 로깅 (5xx 에러용, 스택트레이스 포함)
     */
    protected fun logError(
        tag: String,
        e: Exception,
        request: HttpServletRequest,
    ) {
        log.error(
            "[{}] path={}, message={}",
            tag,
            request.requestURI,
            e.message,
            e,
        )
    }
}
