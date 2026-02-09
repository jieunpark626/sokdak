package com.sokdak.randomchat.adapter.inbound.websocket.interceptors

import com.sokdak.common.constants.HttpHeaders
import com.sokdak.common.constants.SessionAttributes
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class WebSocketAuthInterceptor(
    @Value("\${gateway.security.token:}")
    private val gatewaySecurityToken: String,
) : HandshakeInterceptor {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>,
    ): Boolean {
        val headers = request.headers

        // Gateway 토큰 검증
        val gatewayToken = headers.getFirst(HttpHeaders.GATEWAY_TOKEN)
        if (gatewaySecurityToken.isBlank() || gatewayToken != gatewaySecurityToken) {
            log.warn("Invalid gateway token for WebSocket connection")
            return false
        }

        // 사용자 ID 추출
        val userId = headers.getFirst(HttpHeaders.USER_ID)
        if (userId.isNullOrBlank()) {
            log.warn("Missing user ID for WebSocket connection")
            return false
        }

        attributes[SessionAttributes.USER_ID] = userId
        log.info("WebSocket handshake successful: userId={}", userId)
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?,
    ) {
        // No-op
    }
}
