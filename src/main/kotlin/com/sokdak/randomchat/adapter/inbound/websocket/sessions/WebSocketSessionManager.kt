package com.sokdak.randomchat.adapter.inbound.websocket.sessions

import com.sokdak.common.domain.valueobjects.UserId
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketSessionManager {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    companion object {
        private const val SEND_TIME_LIMIT = 5000
        private const val BUFFER_SIZE_LIMIT = 512 * 1024
    }

    fun add(
        userId: UserId,
        session: WebSocketSession,
    ) {
        sessions[userId.value] = ConcurrentWebSocketSessionDecorator(session, SEND_TIME_LIMIT, BUFFER_SIZE_LIMIT)
    }

    fun remove(userId: UserId) {
        sessions.remove(userId.value)
    }

    fun get(userId: UserId): WebSocketSession? {
        return sessions[userId.value]
    }
}
