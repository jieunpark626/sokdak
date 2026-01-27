package com.sokdak.auth.adapter.inbound.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sokdak.auth.adapter.inbound.api.dto.requests.LoginRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.RegisterUserRequest
import com.sokdak.auth.application.dto.AuthTokenDto
import com.sokdak.auth.application.dto.LoginResult
import com.sokdak.auth.application.dto.UserDto
import com.sokdak.auth.application.usecases.LoginUseCase
import com.sokdak.auth.application.usecases.LogoutUseCase
import com.sokdak.auth.application.usecases.RefreshTokenUseCase
import com.sokdak.auth.application.usecases.RegisterUserUseCase
import com.sokdak.auth.application.usecases.ResendVerificationEmailUseCase
import com.sokdak.auth.application.usecases.VerifyEmailUseCase
import com.sokdak.auth.application.usecases.VerifyTokenUseCase
import com.sokdak.auth.domain.entities.User
import com.sokdak.auth.domain.enums.Gender
import com.sokdak.auth.domain.enums.Plan
import com.sokdak.auth.domain.services.TokenService
import com.sokdak.auth.domain.valueobjects.Email
import com.sokdak.auth.domain.valueobjects.HashedPassword
import com.sokdak.auth.domain.valueobjects.LoginId
import com.sokdak.auth.domain.valueobjects.UserId
import com.sokdak.config.CustomAuthenticationEntryPoint
import com.sokdak.config.GatewayAuthenticationFilter
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@WebMvcTest(AuthController::class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController 테스트")
class AuthControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var registerUserUseCase: RegisterUserUseCase

    @MockitoBean
    private lateinit var loginUseCase: LoginUseCase

    @MockitoBean
    private lateinit var refreshTokenUseCase: RefreshTokenUseCase

    @MockitoBean
    private lateinit var verifyTokenUseCase: VerifyTokenUseCase

    @MockitoBean
    private lateinit var logoutUseCase: LogoutUseCase

    @MockitoBean
    private lateinit var verifyEmailUseCase: VerifyEmailUseCase

    @MockitoBean
    private lateinit var resendVerificationEmailUseCase: ResendVerificationEmailUseCase

    @MockitoBean
    private lateinit var gatewayAuthenticationFilter: GatewayAuthenticationFilter

    @MockitoBean
    private lateinit var customAuthenticationEntryPoint: CustomAuthenticationEntryPoint

    @MockitoBean
    private lateinit var tokenService: TokenService

    private fun createTestUser(): User =
        User.restore(
            id = UserId("test-user-id"),
            loginId = LoginId("testuser"),
            email = Email("test@example.com"),
            hashedPassword = HashedPassword("hashed-password"),
            name = "테스트유저",
            gender = Gender.MALE,
            plan = Plan.FREE,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            emailVerified = false,
        )

    @Nested
    @DisplayName("POST /auth/register - 회원가입")
    inner class Register {
        @Test
        fun `회원가입 성공시 201 반환`() {
            // given
            val request =
                RegisterUserRequest(
                    loginId = "testuser",
                    email = "test@example.com",
                    password = "password123",
                    name = "테스트유저",
                    gender = "MALE",
                )
            given(registerUserUseCase.execute(any())).willReturn(createTestUser())

            // when & then
            mockMvc
                .perform(
                    post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").value("test-user-id"))
                .andExpect(jsonPath("$.name").value("테스트유저"))
        }

        @Test
        fun `필수 필드 누락시 400 반환`() {
            val request =
                mapOf(
                    "loginId" to "",
                    "email" to "test@example.com",
                    "password" to "password123",
                    "name" to "테스트유저",
                    "gender" to "MALE",
                )

            mockMvc
                .perform(
                    post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `잘못된 이메일 형식시 400 반환`() {
            val request =
                RegisterUserRequest(
                    loginId = "testuser",
                    email = "invalid-email",
                    password = "password123",
                    name = "테스트유저",
                    gender = "MALE",
                )

            mockMvc
                .perform(
                    post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                )
                .andExpect(status().isBadRequest)
        }
    }

    @Nested
    @DisplayName("POST /auth/login - 로그인")
    inner class Login {
        @Test
        fun `로그인 성공시 200 반환`() {
            // given
            val request =
                LoginRequest(
                    loginId = "testuser",
                    password = "password123",
                )
            val loginResult =
                LoginResult(
                    user = UserDto(userId = "test-user-id", name = "테스트유저"),
                    tokens =
                        AuthTokenDto(
                            accessToken = "access-token",
                            refreshToken = "refresh-token",
                            expiresInSeconds = 3600,
                        ),
                )
            given(loginUseCase.execute(any())).willReturn(loginResult)

            // when & then
            mockMvc
                .perform(
                    post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.user.id").value("test-user-id"))
                .andExpect(jsonPath("$.tokens.accessToken").value("access-token"))
        }

        @Test
        fun `필수 필드 누락시 400 반환`() {
            val request =
                mapOf(
                    "loginId" to "",
                    "password" to "password123",
                )

            mockMvc
                .perform(
                    post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                )
                .andExpect(status().isBadRequest)
        }
    }
}
