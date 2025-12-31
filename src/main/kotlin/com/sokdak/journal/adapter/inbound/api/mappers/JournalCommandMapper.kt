package com.sokdak.journal.adapter.inbound.api.mappers

import com.sokdak.journal.adapter.inbound.api.dto.requests.CreateJournalRequest
import com.sokdak.journal.adapter.inbound.api.dto.requests.GetJournalsRequest
import com.sokdak.journal.adapter.inbound.api.dto.requests.UpdateJournalRequest
import com.sokdak.journal.application.commands.CreateJournalCommand
import com.sokdak.journal.application.commands.GetJournalsCommand
import com.sokdak.journal.application.commands.UpdateJournalCommand
import com.sokdak.user.domain.valueobjects.UserId

fun CreateJournalRequest.toCommand(): CreateJournalCommand {
    return CreateJournalCommand(
        userId = UserId(this.userId),
        title = this.title,
        content = this.content,
    )
}

fun UpdateJournalRequest.toCommand(): UpdateJournalCommand =
    UpdateJournalCommand(
        title = this.title,
        content = this.content,
    )

fun GetJournalsRequest.toCommand(): GetJournalsCommand =
    GetJournalsCommand(
        userId = this.userId?.let { UserId(it) },
        startDate = this.startDate,
        endDate = this.endDate,
        keyword = this.keyword,
        page = this.page,
        size = this.size,
    )
