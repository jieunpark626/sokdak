package com.sokdak.auth.application.usecases

import com.sokdak.auth.application.commands.RegisterUserCommand
import com.sokdak.auth.application.exceptions.DuplicateEmailException
import com.sokdak.auth.application.exceptions.DuplicateLoginIdException
import com.sokdak.auth.domain.entities.User
import com.sokdak.auth.domain.enums.Gender
import com.sokdak.auth.domain.repositories.UserRepository
import com.sokdak.auth.domain.services.PasswordService
import com.sokdak.auth.domain.valueobjects.Email
import com.sokdak.auth.domain.valueobjects.LoginId
import com.sokdak.auth.domain.valueobjects.RawPassword
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterUserUseCase(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
) {
    @Transactional
    fun execute(command: RegisterUserCommand): User {
        // Value Object 생성
        val loginId = LoginId(command.loginId)
        val email = Email(command.email)

        // 중복 체크
        if (userRepository.existsByLoginId(loginId)) {
            throw DuplicateLoginIdException("Login ID already exists: ${command.loginId}")
        }

        if (userRepository.existsByEmail(email)) {
            throw DuplicateEmailException("Email already exists: ${command.email}")
        }

        // 비밀번호 검증 및 해싱
        val rawPassword = RawPassword.of(command.password)
        val hashedPassword = passwordService.hash(rawPassword)

        // 사용자 생성
        val user =
            User.create(
                loginId = command.loginId,
                email = command.email,
                hashedPassword = hashedPassword,
                name = command.name,
                gender = Gender.valueOf(command.gender.uppercase()),
            )

        return userRepository.save(user)
    }
}
