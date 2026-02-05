package com.sokdak.randomchat.application.exceptions

enum class ChatErrorCode(
    val code: String,
    val message: String,
) {
    ALREADY_IN_QUEUE("ALREADY_IN_QUEUE", "이미 대기열에 등록되어 있습니다."),
    ALREADY_IN_ROOM("ALREADY_IN_ROOM", "이미 채팅 중입니다."),
    NOT_IN_QUEUE("NOT_IN_QUEUE", "대기열에 등록되어 있지 않습니다."),
    NOT_IN_ROOM("NOT_IN_ROOM", "채팅방에 참여하고 있지 않습니다."),
    ROOM_NOT_FOUND("ROOM_NOT_FOUND", "채팅방을 찾을 수 없습니다."),
    INVALID_MESSAGE("INVALID_MESSAGE", "잘못된 메시지 형식입니다."),
    MESSAGE_TOO_LONG("MESSAGE_TOO_LONG", "메시지가 너무 깁니다."),
    INTERNAL_ERROR("INTERNAL_ERROR", "서버 오류가 발생했습니다."),
}
