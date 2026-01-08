package com.sokdak.limit.application.exceptions

class LimitExceededException(
    userId: String,
    action: String,
) : RuntimeException("Daily limit exceeded for user $userId on action $action")

class LimitNotFoundException(
    userId: String,
    action: String,
) : RuntimeException("Limit not found for user $userId on action $action")
