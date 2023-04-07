package team.aliens.dms.domain.notice.dto

import java.time.LocalDateTime
import java.util.UUID

data class QueryAllNoticesResponse(
    val notices: List<NoticeDetails>
) {

    data class NoticeDetails(
        val id: UUID,
        val title: String,
        val createdAt: LocalDateTime
    )
}
