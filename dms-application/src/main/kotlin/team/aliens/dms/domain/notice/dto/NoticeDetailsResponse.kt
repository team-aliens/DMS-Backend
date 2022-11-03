package team.aliens.dms.domain.notice.dto

import java.time.LocalDateTime

data class NoticeDetailsResponse(
    val title: String,
    val content: String,
    val createdAt: LocalDateTime
)
