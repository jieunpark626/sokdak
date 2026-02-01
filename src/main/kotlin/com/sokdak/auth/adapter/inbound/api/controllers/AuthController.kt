package com.sokdak.auth.adapter.inbound.api.controllers

import com.sokdak.auth.adapter.inbound.api.dto.requests.LoginRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.RegisterUserRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.ResendVerificationEmailRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.VerifyTokenRequest
import com.sokdak.auth.adapter.inbound.api.dto.responses.LoginResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.TokenResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.UserResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.VerifyEmailResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.VerifyTokenResponse
import com.sokdak.auth.adapter.inbound.api.mappers.toCommand
import com.sokdak.auth.adapter.inbound.api.mappers.toResponse
import com.sokdak.auth.application.commands.LogoutCommand
import com.sokdak.auth.application.commands.RefreshTokenCommand
import com.sokdak.auth.application.commands.VerifyEmailCommand
import com.sokdak.auth.application.usecases.LoginUseCase
import com.sokdak.auth.application.usecases.LogoutUseCase
import com.sokdak.auth.application.usecases.RefreshTokenUseCase
import com.sokdak.auth.application.usecases.RegisterUserUseCase
import com.sokdak.auth.application.usecases.ResendVerificationEmailUseCase
import com.sokdak.auth.application.usecases.VerifyEmailUseCase
import com.sokdak.auth.application.usecases.VerifyTokenUseCase
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/auth")
class AuthController(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val verifyTokenUseCase: VerifyTokenUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val resendVerificationEmailUseCase: ResendVerificationEmailUseCase,
    @Value("\${gateway.base-url}") private val gatewayBaseUrl: String,
    @Value("\${cookie.secure:true}") private val cookieSecure: Boolean,
) {
    companion object {
        private const val REFRESH_TOKEN_COOKIE_NAME = "refresh_token"
    }

    @ResponseBody
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterUserRequest,
    ): ResponseEntity<UserResponse> {
        val command = request.toCommand()
        val user = registerUserUseCase.execute(command)
        val response = user.toResponse()

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response)
    }

    @ResponseBody
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
        response: HttpServletResponse,
    ): ResponseEntity<LoginResponse> {
        val command = request.toCommand()
        val result = loginUseCase.execute(command)

        setRefreshTokenCookie(response, result.tokens.refreshToken, result.tokens.refreshTokenExpiresInSeconds)
        
        return ResponseEntity.ok(result.toResponse())
    }

    @ResponseBody
    @PostMapping("/refresh")
    fun refresh(
        @CookieValue(REFRESH_TOKEN_COOKIE_NAME) refreshToken: String,
        response: HttpServletResponse,
    ): ResponseEntity<TokenResponse> {
        val command = RefreshTokenCommand(refreshToken = refreshToken)
        val tokens = refreshTokenUseCase.execute(command)

        setRefreshTokenCookie(response, tokens.refreshToken, tokens.refreshTokenExpiresInSeconds)

        return ResponseEntity.ok(tokens.toResponse())
    }

    @ResponseBody
    @PostMapping("/verify")
    fun verify(
        @Valid @RequestBody request: VerifyTokenRequest,
    ): ResponseEntity<VerifyTokenResponse> {
        val command = request.toCommand()
        val result = verifyTokenUseCase.execute(command)
        val response = result.toResponse()

        return ResponseEntity.ok(response)
    }

    @ResponseBody
    @PostMapping("/logout")
    fun logout(
        @CookieValue(REFRESH_TOKEN_COOKIE_NAME, required = false) refreshToken: String?,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        refreshToken?.let {
            logoutUseCase.execute(LogoutCommand(refreshToken = it))
        }

        clearRefreshTokenCookie(response)

        return ResponseEntity.noContent().build()
    }

    private fun setRefreshTokenCookie(
        response: HttpServletResponse,
        refreshToken: String,
        maxAgeSeconds: Long,
    ) {
        val cookie =
            ResponseCookie
                .from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/auth")
                .maxAge(maxAgeSeconds)
                .build()
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
    }

    private fun clearRefreshTokenCookie(response: HttpServletResponse) {
        val cookie =
            ResponseCookie
                .from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .path("/auth")
                .maxAge(0)
                .build()
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString())
    }

    @GetMapping("/verify-email")
    fun verifyEmailGet(
        @RequestParam token: String,
    ): String {
        return try {
            val command = VerifyEmailCommand(token = token)
            verifyEmailUseCase.execute(command)
            "redirect:$gatewayBaseUrl/auth/verify-success"
        } catch (e: Exception) {
            val errorMessage = e.message ?: "이메일 인증에 실패했습니다."
            "redirect:$gatewayBaseUrl/auth/verify-error?message=$errorMessage"
        }
    }

    @GetMapping("/verify-success")
    fun verifySuccess(): ModelAndView {
        return ModelAndView("verify-email-result").apply {
            addObject("success", true)
            addObject("message", "이메일 인증이 성공적으로 완료되었습니다.")
        }
    }

    @GetMapping("/verify-error")
    fun verifyError(
        @RequestParam(required = false, defaultValue = "이메일 인증에 실패했습니다.") message: String,
    ): ModelAndView {
        return ModelAndView("verify-email-result").apply {
            addObject("success", false)
            addObject("message", message)
        }
    }

    @ResponseBody
    @PostMapping("/resend-verification-email")
    fun resendVerificationEmail(
        @Valid @RequestBody request: ResendVerificationEmailRequest,
    ): ResponseEntity<VerifyEmailResponse> {
        val command = request.toCommand()
        resendVerificationEmailUseCase.execute(command)

        return ResponseEntity.ok(VerifyEmailResponse(message = "Verification email sent successfully!"))
    }
}
