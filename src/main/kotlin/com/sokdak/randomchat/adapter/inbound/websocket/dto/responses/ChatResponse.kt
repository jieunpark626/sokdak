package com.sokdak.randomchat.adapter.inbound.websocket.dto.responses

sealed class ChatResponse {
    abstract val type: String
    abstract val timestamp: Long

    data class QueueJoined(
        val position: Int,
        override val timestamp: Long = System.currentTimeMillis(),
    ) : ChatResponse() {
        override val type: String = "QUEUE_JOINED"
    }

    data class QueueLeft(
        override val timestamp: Long = System.currentTimeMillis(),
    ) : ChatResponse() {
        override val type: String = "QUEUE_LEFT"
    }

    data class Matched(
        val roomId: String,
        override val timestamp: Long = System.currentTimeMillis(),
    ) : ChatResponse() {
        override val type: String = "MATCHED"
    }

    data class MessageReceived(
        val messageId: String,
        val content: String,
        override val timestamp: Long = System.currentTimeMillis(),
    ) : ChatResponse() {
        override val type: String = "MESSAGE_RECEIVED"
    }

    data class PartnerLeft(
        override val timestamp: Long = System.currentTimeMillis(),
    ) : ChatResponse() {
        override val type: String = "PARTNER_LEFT"
    }

    data class Error(
        val code: String,
        val message: String,
        override val timestamp: Long = System.currentTimeMillis(),
    ) : ChatResponse() {
        override val type: String = "ERROR"
    }
}
