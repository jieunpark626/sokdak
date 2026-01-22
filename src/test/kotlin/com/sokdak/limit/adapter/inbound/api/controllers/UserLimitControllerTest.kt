package com.sokdak.limit.adapter.inbound.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sokdak.auth.domain.services.TokenService
import com.sokdak.config.CustomAuthenticationEntryPoint
import com.sokdak.config.GatewayAuthenticationFilter
import com.sokdak.config.JwtAuthenticationFilter
import com.sokdak.limit.adapter.inbound.api.dto.requests.ConsumeUsageLimitRequest
import com.sokdak.limit.application.usecases.ConsumeUsageLimitUseCase
import com.sokdak.limit.application.usecases.GetUserLimitsUseCase
import com.sokdak.limit.domain.entities.UserUsageLimit
import com.sokdak.limit.domain.enums.ActionType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.time.LocalDate

@WebMvcTest(UserLimitController::class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UserLimitController 테스트")
class UserLimitControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var getUserLimitsUseCase: GetUserLimitsUseCase

    @MockBean
    private lateinit var consumeUsageLimitUseCase: ConsumeUsageLimitUseCase

    @MockBean
    private lateinit var gatewayAuthenticationFilter: GatewayAuthenticationFilter

    @MockBean
    private lateinit var customAuthenticationEntryPoint: CustomAuthenticationEntryPoint

    @MockBean
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @MockBean
    private lateinit var tokenService: TokenService

    private val testLimit =
        UserUsageLimit(
            id = "test-limit-id",
            userId = "test-user-id",
            action = ActionType.AI_CHAT,
            dailyLimit = 10,
            dailyUsed = 3,
            lastResetDate = LocalDate.now(),
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )

    @Nested
    @DisplayName("GET /users/{userId}/limits - 사용량 조회")
    inner class GetUserLimits {
        @Test
        @WithMockUser
        fun `사용량 조회 성공시 200 반환`() {
            // given
            given(getUserLimitsUseCase.execute("test-user-id")).willReturn(listOf(testLimit))

            // when & then
            mockMvc
                .perform(get("/users/test-user-id/limits"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$[0].userId").value("test-user-id"))
                .andExpect(jsonPath("$[0].action").value("AI_CHAT"))
                .andExpect(jsonPath("$[0].dailyLimit").value(10))
                .andExpect(jsonPath("$[0].dailyUsed").value(3))
                .andExpect(jsonPath("$[0].remaining").value(7))
        }

    }

    @Nested
    @DisplayName("POST /users/{userId}/limits/consume - 사용량 소비")
    inner class ConsumeUsageLimit {
        @Test
        @WithMockUser
        fun `사용량 소비 성공시 200 반환`() {
            // given
            val request =
                ConsumeUsageLimitRequest(
                    action = ActionType.AI_CHAT,
                    count = 1,
                )
            val consumedLimit = testLimit.copy(dailyUsed = 4)
            given(consumeUsageLimitUseCase.execute(any())).willReturn(consumedLimit)

            // when & then
            mockMvc
                .perform(
                    post("/users/test-user-id/limits/consume")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.dailyUsed").value(4))
                .andExpect(jsonPath("$.remaining").value(6))
        }
    }
}