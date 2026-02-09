package com.sokdak.auth.domain.repositories

import com.sokdak.auth.domain.entities.User
import com.sokdak.auth.domain.valueobjects.Email
import com.sokdak.auth.domain.valueobjects.LoginId
import com.sokdak.common.domain.valueobjects.UserId

interface UserRepository {
    fun save(user: User): User

    fun findById(id: UserId): User?

    fun findByLoginId(loginId: LoginId): User?

    fun findByEmail(email: Email): User?

    fun existsByLoginId(loginId: LoginId): Boolean

    fun existsByEmail(email: Email): Boolean
}
