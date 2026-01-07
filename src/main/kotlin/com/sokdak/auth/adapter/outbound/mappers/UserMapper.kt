package com.sokdak.auth.adapter.outbound.mappers

import com.sokdak.auth.adapter.outbound.entities.UserJpaEntity
import com.sokdak.auth.domain.entities.User
import com.sokdak.auth.domain.enums.Gender
import com.sokdak.auth.domain.enums.Plan
import com.sokdak.auth.domain.valueobjects.Email
import com.sokdak.auth.domain.valueobjects.HashedPassword
import com.sokdak.auth.domain.valueobjects.LoginId
import com.sokdak.auth.domain.valueobjects.UserId
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDomain(entity: UserJpaEntity): User {
        return User.restore(
            id = UserId(entity.id),
            loginId = LoginId(entity.loginId),
            email = Email(entity.email),
            hashedPassword = HashedPassword(entity.passwordHash),
            name = entity.name,
            gender = Gender.valueOf(entity.gender),
            plan = Plan.valueOf(entity.plan),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            emailVerified = entity.emailVerified,
        )
    }

    fun toJpaEntity(domain: User): UserJpaEntity {
        return UserJpaEntity(
            id = domain.id.value,
            loginId = domain.loginId.value,
            email = domain.email.value,
            passwordHash = domain.passwordHash.value,
            name = domain.name,
            gender = domain.gender.name,
            plan = domain.plan.name,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            emailVerified = domain.emailVerified,
        )
    }
}
