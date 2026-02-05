package com.sokdak.randomchat.application.exceptions

sealed class ChatException(
    val errorCode: ChatErrorCode,
) : RuntimeException(errorCode.message)

class AlreadyInQueueException : ChatException(ChatErrorCode.ALREADY_IN_QUEUE)

class AlreadyInRoomException : ChatException(ChatErrorCode.ALREADY_IN_ROOM)

class NotInQueueException : ChatException(ChatErrorCode.NOT_IN_QUEUE)

class NotInRoomException : ChatException(ChatErrorCode.NOT_IN_ROOM)

class RoomNotFoundException : ChatException(ChatErrorCode.ROOM_NOT_FOUND)

class InvalidMessageException : ChatException(ChatErrorCode.INVALID_MESSAGE)

class MessageTooLongException : ChatException(ChatErrorCode.MESSAGE_TOO_LONG)
