package com.sokdak.journal.adapter.inbound.api.controllers

import com.sokdak.journal.adapter.inbound.api.dto.requests.CreateJournalRequest
import com.sokdak.journal.adapter.inbound.api.dto.requests.GetJournalsRequest
import com.sokdak.journal.adapter.inbound.api.dto.requests.UpdateJournalRequest
import com.sokdak.journal.adapter.inbound.api.dto.responses.JournalResponse
import com.sokdak.journal.adapter.inbound.api.dto.responses.JournalSummaryResponse
import com.sokdak.journal.adapter.inbound.api.mappers.toCommand
import com.sokdak.journal.adapter.inbound.api.mappers.toResponse
import com.sokdak.journal.adapter.inbound.api.mappers.toSummaryResponse
import com.sokdak.journal.application.usecases.CreateJournalUseCase
import com.sokdak.journal.application.usecases.DeleteJournalUseCase
import com.sokdak.journal.application.usecases.GetJournalUseCase
import com.sokdak.journal.application.usecases.GetJournalsUseCase
import com.sokdak.journal.application.usecases.UpdateJournalUseCase
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/journals")
class JournalController(
    private val createJournalUseCase: CreateJournalUseCase,
    private val getJournalsUseCase: GetJournalsUseCase,
    private val getJournalUseCase: GetJournalUseCase,
    private val updateJournalUseCase: UpdateJournalUseCase,
    private val deleteJournalUseCase: DeleteJournalUseCase,
) {
    /**
     * 1. 일기 작성
     * POST /journals
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createJournal(
        @RequestBody request: CreateJournalRequest,
    ): ResponseEntity<JournalResponse> {
        val journal = createJournalUseCase.execute(request.toCommand())
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(journal.toResponse())
    }

    /**
     * 2. 일기 목록 조회 (Query DTO)
     * GET /journals
     */
    @GetMapping
    fun getJournals(
        // 쿼리 파라미터
        request: GetJournalsRequest,
    ): Page<JournalSummaryResponse> {
        val page = getJournalsUseCase.execute(request.toCommand())
        return page.map { it.toSummaryResponse() }
    }

    /**
     * 3. 특정 일기 조회
     * GET /journals/{journalId}
     */
    @GetMapping("/{journalId}")
    fun getJournal(
        @PathVariable journalId: String,
    ): JournalResponse {
        val journal = getJournalUseCase.execute(journalId)
        return journal.toResponse()
    }

    /**
     * 4. 일기 수정
     * PATCH /journals/{journalId}
     */
    @PatchMapping("/{journalId}")
    fun updateJournal(
        @PathVariable journalId: String,
        @RequestBody request: UpdateJournalRequest,
    ): JournalResponse {
        val journal =
            updateJournalUseCase.execute(
                id = journalId,
                command = request.toCommand(),
            )
        return journal.toResponse()
    }

    /**
     * 5. 일기 삭제
     * DELETE /journals/{journalId}
     */
    @DeleteMapping("/{journalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteJournal(
        @PathVariable journalId: String,
    ) {
        deleteJournalUseCase.execute(journalId)
    }
}
