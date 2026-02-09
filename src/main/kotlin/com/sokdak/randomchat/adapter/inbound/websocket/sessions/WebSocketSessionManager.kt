package com.sokdak.randomchat.adapter.inbound.websocket.sessions

import com.sokdak.common.domain.valueobjects.UserId
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketSessionManager {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    fun add(
        userId: UserId,
        session: WebSocketSession,
    ) {
        sessions[userId.value] = session
    }

    fun remove(userId: UserId) {
        sessions.remove(userId.value)
    }

    fun get(userId: UserId): WebSocketSession? {
        return sessions[userId.value]
    }
}
