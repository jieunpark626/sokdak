package com.sokdak.journal.adapter.inbound.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sokdak.journal.adapter.inbound.api.dto.requests.CreateJournalRequest
import com.sokdak.journal.adapter.inbound.api.dto.requests.UpdateJournalRequest
import com.sokdak.auth.domain.services.TokenService
import com.sokdak.config.CustomAuthenticationEntryPoint
import com.sokdak.config.GatewayAuthenticationFilter
import com.sokdak.config.JwtAuthenticationFilter
import com.sokdak.journal.application.usecases.CreateJournalUseCase
import com.sokdak.journal.application.usecases.DeleteJournalUseCase
import com.sokdak.journal.application.usecases.GetJournalUseCase
import com.sokdak.journal.application.usecases.GetJournalsUseCase
import com.sokdak.journal.application.usecases.UpdateJournalUseCase
import com.sokdak.journal.domain.entities.Journal
import com.sokdak.journal.domain.valueobjects.JournalId
import com.sokdak.user.domain.valueobjects.UserId
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willDoNothing
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@WebMvcTest(JournalController::class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("JournalController 테스트")
class JournalControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var createJournalUseCase: CreateJournalUseCase

    @MockBean
    private lateinit var getJournalsUseCase: GetJournalsUseCase

    @MockBean
    private lateinit var getJournalUseCase: GetJournalUseCase

    @MockBean
    private lateinit var updateJournalUseCase: UpdateJournalUseCase

    @MockBean
    private lateinit var deleteJournalUseCase: DeleteJournalUseCase

    @MockBean
    private lateinit var gatewayAuthenticationFilter: GatewayAuthenticationFilter

    @MockBean
    private lateinit var customAuthenticationEntryPoint: CustomAuthenticationEntryPoint

    @MockBean
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @MockBean
    private lateinit var tokenService: TokenService

    private fun createTestJournal(
        id: String = "test-journal-id",
        userId: String = "test-user-id",
        title: String = "테스트 일기",
        content: String = "테스트 내용입니다.",
    ): Journal =
        Journal(
            id = JournalId(id),
            userId = UserId(userId),
            title = title,
            content = content,
            createdAt = Instant.parse("2024-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2024-01-01T00:00:00Z"),
        )

    @Nested
    @DisplayName("POST /journals - 일기 생성")
    inner class CreateJournal {
        @Test
        @WithMockUser
        fun `일기 생성 성공시 201 반환`() {
            // given
            val request =
                CreateJournalRequest(
                    userId = "test-user-id",
                    title = "테스트 일기",
                    content = "테스트 내용입니다.",
                )
            given(createJournalUseCase.execute(any())).willReturn(createTestJournal())

            // when & then
            mockMvc
                .perform(
                    post("/journals")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").value("test-journal-id"))
                .andExpect(jsonPath("$.title").value("테스트 일기"))
                .andExpect(jsonPath("$.content").value("테스트 내용입니다."))
        }

    }

    @Nested
    @DisplayName("GET /journals/{journalId} - 일기 단건 조회")
    inner class GetJournal {
        @Test
        @WithMockUser
        fun `일기 조회 성공시 200 반환`() {
            // given
            given(getJournalUseCase.execute("test-journal-id")).willReturn(createTestJournal())

            // when & then
            mockMvc
                .perform(get("/journals/test-journal-id"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value("test-journal-id"))
                .andExpect(jsonPath("$.title").value("테스트 일기"))
        }
    }

    @Nested
    @DisplayName("PATCH /journals/{journalId} - 일기 수정")
    inner class UpdateJournal {
        @Test
        @WithMockUser
        fun `일기 수정 성공시 200 반환`() {
            // given
            val request =
                UpdateJournalRequest(
                    title = "수정된 제목",
                    content = "수정된 내용",
                )
            val updatedJournal = createTestJournal(title = "수정된 제목", content = "수정된 내용")
            given(updateJournalUseCase.execute(any(), any())).willReturn(updatedJournal)

            // when & then
            mockMvc
                .perform(
                    patch("/journals/test-journal-id")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)),
                )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.content").value("수정된 내용"))
        }
    }

    @Nested
    @DisplayName("DELETE /journals/{journalId} - 일기 삭제")
    inner class DeleteJournal {
        @Test
        @WithMockUser
        fun `일기 삭제 성공시 204 반환`() {
            // given
            willDoNothing().given(deleteJournalUseCase).execute("test-journal-id")

            // when & then
            mockMvc
                .perform(
                    delete("/journals/test-journal-id")
                        .with(csrf()),
                )
                .andExpect(status().isNoContent)
        }
    }
}