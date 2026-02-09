package com.sokdak.randomchat.adapter.inbound.websocket.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.sokdak.common.constants.SessionAttributes
import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.adapter.inbound.websocket.dto.requests.ChatRequest
import com.sokdak.randomchat.adapter.inbound.websocket.dto.responses.ChatResponse
import com.sokdak.randomchat.adapter.inbound.websocket.sessions.WebSocketSessionManager
import com.sokdak.randomchat.application.exceptions.ChatException
import com.sokdak.randomchat.application.usecases.ExitChatRoomUseCase
import com.sokdak.randomchat.application.usecases.HandleDisconnectionUseCase
import com.sokdak.randomchat.application.usecases.JoinAndMatchUseCase
import com.sokdak.randomchat.application.usecases.LeaveWaitingQueueUseCase
import com.sokdak.randomchat.application.usecases.SendMessageUseCase
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
    private val joinAndMatchUseCase: JoinAndMatchUseCase,
    private val leaveWaitingQueueUseCase: LeaveWaitingQueueUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val exitChatRoomUseCase: ExitChatRoomUseCase,
    private val handleDisconnectionUseCase: HandleDisconnectionUseCase,
) : TextWebSocketHandler() {
    private val log = LoggerFactory.getLogger(javaClass)

    // 1. 클라이언트가 접속했을 때 : 세션 맵에 추가해둠
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = extractUserId(session)
        sessionManager.add(userId, session)
        log.info("WebSocket connected: userId={}", userId.value)
    }

    // 2. 메시지를 받았을 때
    override fun handleTextMessage(
        session: WebSocketSession,
        message: TextMessage,
    ) {
        val userId = extractUserId(session)

        try {
            val request = objectMapper.readValue<ChatRequest>(message.payload)
            handleRequest(userId, session, request)
        } catch (e: ChatException) {
            sendResponse(session, ChatResponse.Error(e.errorCode.code, e.errorCode.message))
        } catch (e: Exception) {
            log.error("Error handling message: userId={}", userId.value, e)
            sendResponse(session, ChatResponse.Error("INTERNAL_ERROR", "서버 오류가 발생했습니다."))
        }
    }

    // 3. 접속이 끊겼을 때
    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus,
    ) {
        val userId = extractUserId(session)
        sessionManager.remove(userId)
        try {
            handleDisconnectionUseCase.execute(userId)
        } catch (e: Exception) {
            log.warn("Failed to handle disconnection: userId={}", userId.value, e)
        }
        log.info("WebSocket disconnected: userId={}, status={}", userId.value, status)
    }

    private fun handleRequest(
        userId: UserId,
        session: WebSocketSession,
        request: ChatRequest,
    ) {
        when (request) {
            // 1. 랜덤 채팅 시작
            ChatRequest.JoinQueue -> {
                // TODO: 순서 확인, 매치가 되어버리면 매치되었다는 응답 오고 나서 큐 조인 응답이 와버림
                joinAndMatchUseCase.execute(userId)
                sendResponse(session, ChatResponse.QueueJoined())
            }
            // 2. 대기 중 나감
            ChatRequest.LeaveQueue -> {
                leaveWaitingQueueUseCase.execute(userId)
                sendResponse(session, ChatResponse.QueueLeft())
            }

            // 3. 메시지 보냄
            is ChatRequest.SendMessage -> sendMessageUseCase.execute(userId, request.content)

            // 4. 채팅방 나감
            ChatRequest.ExitRoom -> exitChatRoomUseCase.execute(userId)
        }
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
