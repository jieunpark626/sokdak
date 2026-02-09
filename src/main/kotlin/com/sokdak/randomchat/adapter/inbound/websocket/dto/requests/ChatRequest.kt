package com.sokdak.randomchat.adapter.inbound.websocket.dto.requests

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = ChatRequest.JoinQueue::class, name = "JOIN_QUEUE"),
    JsonSubTypes.Type(value = ChatRequest.LeaveQueue::class, name = "LEAVE_QUEUE"),
    JsonSubTypes.Type(value = ChatRequest.SendMessage::class, name = "SEND_MESSAGE"),
    JsonSubTypes.Type(value = ChatRequest.ExitRoom::class, name = "EXIT_ROOM"),
)
sealed class ChatRequest {
    data object JoinQueue : ChatRequest()

    data object LeaveQueue : ChatRequest() // 대기열 이탈 (매칭 도중 나감)

    data class SendMessage(val content: String) : ChatRequest() // 메시지 전송

    data object ExitRoom : ChatRequest() // 방 나가기 (매칭 이후 채팅 중에 나감)
}
