package com.sokdak.randomchat.adapter.outbound.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.adapter.inbound.websocket.dto.responses.ChatResponse
import com.sokdak.randomchat.adapter.inbound.websocket.sessions.WebSocketSessionManager
import com.sokdak.randomchat.domain.services.ChatNotificationService
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage

@Service
class WebSocketChatNotificationService(
    private val objectMapper: ObjectMapper,
    private val sessionManager: WebSocketSessionManager,
) : ChatNotificationService {
    override fun notifyMatched(
        userId: UserId,
        roomId: ChatRoomId,
    ) {
        sendResponse(userId, ChatResponse.Matched(roomId.value))
    }

    override fun notifyMessageReceived(
        userId: UserId,
        messageId: String,
        content: String,
    ) {
        sendResponse(userId, ChatResponse.MessageReceived(messageId, content))
    }

    override fun notifyPartnerLeft(userId: UserId) {
        sendResponse(userId, ChatResponse.PartnerLeft())
    }

    private fun sendResponse(
        userId: UserId,
        response: ChatResponse,
    ) {
        // TODO 세션이 없어서 return 하면 어떤일이 일어나는데?
        val session = sessionManager.get(userId) ?: return
        if (session.isOpen) {
            val json = objectMapper.writeValueAsString(response)
            session.sendMessage(TextMessage(json))
        }
    }
}
