package com.sokdak.auth.application.usecases

import com.sokdak.auth.adapter.outbound.config.EmailProperties
import com.sokdak.auth.application.commands.SendVerificationEmailCommand
import com.sokdak.auth.application.exceptions.EmailSendFailedException
import com.sokdak.auth.application.exceptions.UserNotFoundException
import com.sokdak.auth.domain.entities.EmailVerification
import com.sokdak.auth.domain.repositories.EmailVerificationRepository
import com.sokdak.auth.domain.repositories.UserRepository
import com.sokdak.auth.domain.services.EmailService
import com.sokdak.common.domain.valueobjects.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

// TODO: 비동기로 바꾸거나 이벤트 기반으로 변경하기
@Service
class SendVerificationEmailUseCase(
    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val emailService: EmailService,
    private val emailProperties: EmailProperties,
) {
    @Transactional
    fun execute(command: SendVerificationEmailCommand) {
        val userId = UserId(command.userId)
        val user =
            userRepository.findById(userId)
                ?: throw UserNotFoundException("User not found: ${command.userId}")

        emailVerificationRepository.deleteByUserId(userId)

        val expiresAt = Instant.now().plusSeconds(emailProperties.tokenValidityInSeconds)
        val verification =
            EmailVerification.create(
                userId = user.id,
                email = user.email,
                expiresAt = expiresAt,
            )

        emailVerificationRepository.save(verification)

        try {
            emailService.sendVerificationEmail(
                recipientEmail = user.email,
                recipientName = user.name,
                verificationToken = verification.token,
            )
        } catch (e: Exception) {
            // TODO: 이메일이 가지 않더라도 회원가입을 완료할 수 있도록
            throw EmailSendFailedException("Failed to send verification email: ${e.message}")
        }
    }
}
