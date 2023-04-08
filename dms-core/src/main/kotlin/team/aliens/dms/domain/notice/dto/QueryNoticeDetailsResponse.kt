package team.aliens.dms.domain.notice.dto

import java.time.LocalDateTime
import java.util.UUID

data class QueryNoticeDetailsResponse(
    val id: UUID,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime
)
