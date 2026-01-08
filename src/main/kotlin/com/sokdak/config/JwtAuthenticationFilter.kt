package com.sokdak.config

import com.sokdak.auth.application.exceptions.InvalidTokenException
import com.sokdak.auth.application.exceptions.TokenExpiredException
import com.sokdak.auth.domain.services.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * @deprecated 이 필터는 더 이상 사용되지 않습니다.
 * Gateway에서 JWT 검증을 수행하므로 GatewayAuthenticationFilter를 사용하세요.
 * 이 클래스는 호환성 유지를 위해 남겨두었으며, 향후 버전에서 제거될 예정입니다.
 */
@Deprecated("Use GatewayAuthenticationFilter instead")
@Component
class JwtAuthenticationFilter(
    private val tokenService: TokenService,
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return path.startsWith("/auth/") || path == "/error"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val token = extractTokenFromRequest(request)

            if (token != null) {
                val userId = tokenService.validateToken(token)

                val authentication =
                    UsernamePasswordAuthenticationToken(
                        userId.value,
                        null,
                        emptyList(),
                    )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: TokenExpiredException) {
            logger.debug("Token expired: ${e.message}")
            request.setAttribute("expired", true)
        } catch (e: InvalidTokenException) {
            logger.debug("Invalid token: ${e.message}")
            request.setAttribute("invalid", true)
        }

        filterChain.doFilter(request, response)
    }

    private fun extractTokenFromRequest(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")

        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else {
            null
        }
    }
}
