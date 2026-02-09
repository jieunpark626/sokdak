package com.sokdak.randomchat.domain.valueobjects

sealed class UserChatStatus {
    data object Idle : UserChatStatus()

    data object Waiting : UserChatStatus()

    data class InRoom(val roomId: ChatRoomId) : UserChatStatus()
}
