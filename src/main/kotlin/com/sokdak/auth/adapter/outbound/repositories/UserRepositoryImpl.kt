package com.sokdak.auth.adapter.outbound.repositories

import com.sokdak.auth.adapter.outbound.mappers.UserMapper
import com.sokdak.auth.domain.entities.User
import com.sokdak.auth.domain.repositories.UserRepository
import com.sokdak.auth.domain.valueobjects.Email
import com.sokdak.auth.domain.valueobjects.LoginId
import com.sokdak.auth.domain.valueobjects.UserId
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
    private val mapper: UserMapper,
) : UserRepository {
    override fun save(user: User): User {
        val entity = mapper.toJpaEntity(user)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findById(id: UserId): User? {
        return jpaRepository.findById(id.value)
            .map { mapper.toDomain(it) }
            .orElse(null)
    }

    override fun findByLoginId(loginId: LoginId): User? {
        return jpaRepository.findByLoginId(loginId.value)
            ?.let { mapper.toDomain(it) }
    }

    override fun findByEmail(email: Email): User? {
        return jpaRepository.findByEmail(email.value)
            ?.let { mapper.toDomain(it) }
    }

    override fun existsByLoginId(loginId: LoginId): Boolean {
        return jpaRepository.existsByLoginId(loginId.value)
    }

    override fun existsByEmail(email: Email): Boolean {
        return jpaRepository.existsByEmail(email.value)
    }
}
