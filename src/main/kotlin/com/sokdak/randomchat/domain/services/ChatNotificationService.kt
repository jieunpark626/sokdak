package com.sokdak.randomchat.domain.services

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId

interface ChatNotificationService {
    fun notifyMatched(
        userId: UserId,
        roomId: ChatRoomId,
    )

    fun notifyMessageReceived(
        userId: UserId,
        messageId: String,
        content: String,
    )

    fun notifyPartnerLeft(userId: UserId)
}
