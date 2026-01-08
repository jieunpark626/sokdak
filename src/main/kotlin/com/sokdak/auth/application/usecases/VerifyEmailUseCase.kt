package com.sokdak.auth.application.usecases

import com.sokdak.auth.application.commands.VerifyEmailCommand
import com.sokdak.auth.application.exceptions.EmailAlreadyVerifiedException
import com.sokdak.auth.application.exceptions.VerificationTokenExpiredException
import com.sokdak.auth.application.exceptions.VerificationTokenNotFoundException
import com.sokdak.auth.domain.repositories.EmailVerificationRepository
import com.sokdak.auth.domain.repositories.UserRepository
import com.sokdak.auth.domain.services.EmailService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VerifyEmailUseCase(
    private val emailVerificationRepository: EmailVerificationRepository,
    private val userRepository: UserRepository,
    private val emailService: EmailService,
) {
    @Transactional
    fun execute(command: VerifyEmailCommand) {
        val verification =
            emailVerificationRepository.findByToken(command.token)
                ?: throw VerificationTokenNotFoundException("Invalid verification token")

        if (verification.isVerified()) {
            throw EmailAlreadyVerifiedException("Email already verified")
        }

        if (verification.isExpired()) {
            throw VerificationTokenExpiredException("Verification token has expired")
        }

        verification.verify()
        emailVerificationRepository.save(verification)

        val user =
            userRepository.findById(verification.userId)
                ?: throw VerificationTokenNotFoundException("User not found for this token")

        user.verifyEmail()
        userRepository.save(user)

        try {
            emailService.sendWelcomeEmail(
                recipientEmail = user.email,
                recipientName = user.name,
            )
        } catch (e: Exception) {
            // Log error but don't fail the verification
        }
    }
}
