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

    data object LeaveQueue : ChatRequest()

    data class SendMessage(val content: String) : ChatRequest()

    data object ExitRoom : ChatRequest()
}
