package team.aliens.dms.domain.notice.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class Notice(

    val id: UUID = UUID(0, 0),

    val managerId: UUID,

    val title: String,

    val content: String,

    val createdAt: LocalDateTime?,

    val updatedAt: LocalDateTime?

) {
    fun updateNotice(title: String, content: String) = Notice(
        id = id,
        managerId = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = LocalDateTime.now()
    )
}
