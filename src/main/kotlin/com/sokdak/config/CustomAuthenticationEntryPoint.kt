package com.sokdak.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val objectMapper = ObjectMapper()

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.contentType = "application/json;charset=UTF-8"

        val expired = request.getAttribute("expired") as? Boolean ?: false
        val invalid = request.getAttribute("invalid") as? Boolean ?: false

        when {
            expired -> {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write(
                    objectMapper.writeValueAsString(
                        mapOf(
                            "error" to "TOKEN_EXPIRED",
                            "message" to "Access token has expired. Please refresh your token.",
                        ),
                    ),
                )
            }
            invalid -> {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write(
                    objectMapper.writeValueAsString(
                        mapOf(
                            "error" to "INVALID_TOKEN",
                            "message" to "Invalid token provided.",
                        ),
                    ),
                )
            }
            else -> {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write(
                    objectMapper.writeValueAsString(
                        mapOf(
                            "error" to "UNAUTHORIZED",
                            "message" to "Authentication required.",
                        ),
                    ),
                )
            }
        }
    }
}
