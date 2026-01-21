package com.sokdak.common.web.exception

import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 전역 예외 처리
 *
 * 도메인별 핸들러가 처리하지 못한 예외를 처리합니다.
 * 처리하는 예외:
 * - MethodArgumentNotValidException (Validation)
 * - IllegalArgumentException (VO 검증)
 * - Exception (500 에러, catch-all)
 *
 * @see BaseExceptionHandler
 */
@RestControllerAdvice
@Order(Int.MAX_VALUE) // 가장 낮은 우선순위 (도메인별 핸들러가 먼저 처리)
class GlobalExceptionHandler : BaseExceptionHandler()
