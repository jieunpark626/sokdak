package com.sokdak.auth.adapter.outbound.repositories

import com.sokdak.auth.adapter.outbound.entities.UserJpaEntity
import com.sokdak.auth.domain.entities.User
import com.sokdak.auth.domain.valueobjects.UserId
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserJpaEntity, String> {
    fun findById(id: UserId): User?

    fun findByLoginId(loginId: String): UserJpaEntity?

    fun findByEmail(email: String): UserJpaEntity?

    fun existsByLoginId(loginId: String): Boolean

    fun existsByEmail(email: String): Boolean
}
