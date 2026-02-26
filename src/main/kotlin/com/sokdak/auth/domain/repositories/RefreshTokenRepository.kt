package com.sokdak.auth.domain.repositories

import com.sokdak.auth.domain.entities.RefreshToken
import com.sokdak.common.domain.valueobjects.UserId

interface RefreshTokenRepository {
    fun save(refreshToken: RefreshToken): RefreshToken

    fun findByToken(token: String): RefreshToken?

    fun findByUserId(userId: UserId): List<RefreshToken>

    fun deleteByToken(token: String)

    fun deleteByUserId(userId: UserId)

    fun deleteExpiredTokens()
}
