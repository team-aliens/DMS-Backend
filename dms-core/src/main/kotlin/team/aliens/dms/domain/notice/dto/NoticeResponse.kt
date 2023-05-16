package team.aliens.dms.domain.notice.dto

import team.aliens.dms.domain.notice.model.Notice
import java.time.LocalDateTime
import java.util.UUID

data class NoticeResponse(
    val id: UUID,
    val title: String,
    val content: String? = null,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(notice: Notice): NoticeResponse {
            return NoticeResponse(
                id = notice.id,
                title = notice.title,
                createdAt = notice.createdAt!!
            )
        }

        fun detailOf(notice: Notice): NoticeResponse {
            return NoticeResponse(
                id = notice.id,
                title = notice.title,
                content = notice.content,
                createdAt = notice.createdAt!!
            )
        }
    }
}

data class NoticesResponse(
    val notices: List<NoticeResponse>
)

data class NoticeIdResponse(
    val noticeId: UUID
)
