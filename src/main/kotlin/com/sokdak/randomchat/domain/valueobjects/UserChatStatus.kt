package com.sokdak.randomchat.domain.valueobjects

sealed class UserChatStatus {
    data object NotInChat : UserChatStatus()

    data object Waiting : UserChatStatus()

    data class InRoom(val roomId: ChatRoomId) : UserChatStatus()
}
