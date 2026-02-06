package com.sokdak.randomchat.adapter.inbound.websocket.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sokdak.common.constants.SessionAttributes
import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.adapter.inbound.websocket.dto.requests.ChatRequest
import com.sokdak.randomchat.adapter.inbound.websocket.dto.responses.ChatResponse
import com.sokdak.randomchat.adapter.inbound.websocket.sessions.WebSocketSessionManager
import com.sokdak.randomchat.application.exceptions.ChatException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class RandomChatWebSocketHandler(
    private val objectMapper: ObjectMapper,
    private val sessionManager: WebSocketSessionManager,
) : TextWebSocketHandler() {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = extractUserId(session)
        sessionManager.add(userId, session)
        log.info("WebSocket connected: userId={}", userId.value)
    }

    override fun handleTextMessage(
        session: WebSocketSession,
        message: TextMessage,
    ) {
        val userId = extractUserId(session)

        try {
            val request = objectMapper.readValue(message.payload, ChatRequest::class.java)
            handleRequest(userId, request)
        } catch (e: ChatException) {
            sendResponse(session, ChatResponse.Error(e.errorCode.code, e.errorCode.message))
        } catch (e: Exception) {
            log.error("Error handling message: userId={}", userId.value, e)
            sendResponse(session, ChatResponse.Error("INTERNAL_ERROR", "서버 오류가 발생했습니다."))
        }
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus,
    ) {
        val userId = extractUserId(session)
        sessionManager.remove(userId)

        handleDisconnection(userId)

        log.info("WebSocket disconnected: userId={}, status={}", userId.value, status)
    }

    private fun handleRequest(
        userId: UserId,
        request: ChatRequest,
    ) {
        when (request) {
            is ChatRequest.JoinQueue -> {
                // TODO: Phase 4에서 구현
                log.info("JoinQueue: userId={}", userId.value)
            }

            is ChatRequest.LeaveQueue -> {
                // TODO: Phase 4에서 구현
                log.info("LeaveQueue: userId={}", userId.value)
            }

            is ChatRequest.SendMessage -> {
                // TODO: Phase 6에서 구현
                log.info("SendMessage: userId={}, content={}", userId.value, request.content)
            }

            is ChatRequest.ExitRoom -> {
                // TODO: Phase 6에서 구현
                log.info("ExitRoom: userId={}", userId.value)
            }
        }
    }

    private fun handleDisconnection(userId: UserId) {
        // TODO: Phase 4, 6에서 구현
        log.info("Handling disconnection: userId={}", userId.value)
    }

    fun sendResponse(
        userId: UserId,
        response: ChatResponse,
    ) {
        val session = sessionManager.get(userId) ?: return
        sendResponse(session, response)
    }

    private fun sendResponse(
        session: WebSocketSession,
        response: ChatResponse,
    ) {
        if (session.isOpen) {
            val json = objectMapper.writeValueAsString(response)
            session.sendMessage(TextMessage(json))
        }
    }

    private fun extractUserId(session: WebSocketSession): UserId {
        val userIdValue = session.attributes[SessionAttributes.USER_ID] as String
        return UserId(userIdValue)
    }
}
