package com.sokdak.auth.application.usecases

import com.sokdak.auth.application.commands.ResendVerificationEmailCommand
import com.sokdak.auth.application.commands.SendVerificationEmailCommand
import com.sokdak.auth.application.exceptions.EmailAlreadyVerifiedException
import com.sokdak.auth.application.exceptions.UserNotFoundException
import com.sokdak.auth.domain.repositories.UserRepository
import com.sokdak.auth.domain.valueobjects.Email
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ResendVerificationEmailUseCase(
    private val userRepository: UserRepository,
    private val sendVerificationEmailUseCase: SendVerificationEmailUseCase,
) {
    @Transactional
    fun execute(command: ResendVerificationEmailCommand) {
        val email = Email(command.email)
        val user =
            userRepository.findByEmail(email)
                ?: throw UserNotFoundException("No user found with email: ${command.email}")

        if (user.emailVerified) {
            throw EmailAlreadyVerifiedException("Email already verified")
        }

        val sendCommand = SendVerificationEmailCommand(userId = user.id.value)
        sendVerificationEmailUseCase.execute(sendCommand)
    }
}
