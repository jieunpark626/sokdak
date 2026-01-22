package com.sokdak.common.web.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestLoggingFilter : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val requestId = UUID.randomUUID().toString().substring(0, 8)
        val startTime = System.currentTimeMillis()

        MDC.put("requestId", requestId)

        try {
            filterChain.doFilter(request, response)
        } finally {
            val duration = System.currentTimeMillis() - startTime
            val status = response.status

            when {
                status >= 500 ->
                    log.error(
                        "[{}] {} {} | status={} | duration={}ms | ip={}",
                        requestId,
                        request.method,
                        request.requestURI,
                        status,
                        duration,
                        request.remoteAddr,
                    )
                status >= 400 ->
                    log.warn(
                        "[{}] {} {} | status={} | duration={}ms | ip={}",
                        requestId,
                        request.method,
                        request.requestURI,
                        status,
                        duration,
                        request.remoteAddr,
                    )
                else ->
                    log.info(
                        "[{}] {} {} | status={} | duration={}ms",
                        requestId,
                        request.method,
                        request.requestURI,
                        status,
                        duration,
                    )
            }

            MDC.clear()
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return path.startsWith("/actuator") || path.startsWith("/health")
    }
}
