package com.sokdak.randomchat.domain.valueobjects

import de.huxhorn.sulky.ulid.ULID

@JvmInline
value class ChatRoomId(val value: String) {
    companion object {
        private val ulid = ULID()

        fun generate(): ChatRoomId = ChatRoomId(ulid.nextULID())
    }
}
