package com.sokdak.auth.domain.services

import com.sokdak.auth.domain.valueobjects.AuthTokens
import com.sokdak.auth.domain.valueobjects.UserId

interface TokenService {
    fun generateTokens(userId: UserId): AuthTokens

    fun validateToken(token: String): UserId
}
