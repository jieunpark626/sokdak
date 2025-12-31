package com.sokdak.auth.domain.entities

import com.sokdak.auth.domain.enums.Gender
import com.sokdak.auth.domain.enums.Plan
import com.sokdak.auth.domain.valueobjects.Email
import com.sokdak.auth.domain.valueobjects.HashedPassword
import com.sokdak.auth.domain.valueobjects.LoginId
import com.sokdak.auth.domain.valueobjects.UserId
import de.huxhorn.sulky.ulid.ULID
import java.time.Instant

class User private constructor(
    val id: UserId,
    val loginId: LoginId,
    val email: Email,
    passwordHash: HashedPassword,
    name: String,
    val gender: Gender,
    plan: Plan,
    val createdAt: Instant,
    updatedAt: Instant
) {
    var passwordHash: HashedPassword = passwordHash
        private set

    var name: String = name
        private set

    var plan: Plan = plan
        private set

    var updatedAt: Instant = updatedAt
        private set

    companion object {
        private val ulid = ULID()
        fun create(
            loginId: String,
            email: String,
            hashedPassword: HashedPassword,
            name: String,
            gender: Gender,
            plan: Plan = Plan.FREE,
        ): User {
            require(name.isNotBlank()) { "Name cannot be blank" }
            val now = Instant.now()

            return User(
                id = UserId.generate(),
                loginId = LoginId(loginId),
                email = Email(email),
                passwordHash = hashedPassword,
                name = name,
                gender = gender,
                plan = plan,
                createdAt = now,
                updatedAt = now
            )
        }

        fun restore(
            id: UserId,
            loginId: LoginId,
            email: Email,
            hashedPassword: HashedPassword,
            name: String,
            gender: Gender,
            plan: Plan,
            createdAt: Instant,
            updatedAt: Instant
        ): User {
            return User(
                id = id,
                loginId = loginId,
                email = email,
                passwordHash = hashedPassword,
                name = name,
                gender = gender,
                plan = plan,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }

    fun changePassword(newHashedPassword: HashedPassword) {
        passwordHash = newHashedPassword
        updateTime()
    }

    fun changeName(newName: String) {
        require(newName.isNotBlank()) { "Name cannot be blank" }
        name = newName
        updateTime()
    }

    fun upgradePlan() {
        plan = Plan.PAID
        updateTime()
    }

    fun downgradePlan() {
        plan = Plan.FREE
        updateTime()
    }

    private fun updateTime() {
        this.updatedAt = Instant.now()
    }
}
