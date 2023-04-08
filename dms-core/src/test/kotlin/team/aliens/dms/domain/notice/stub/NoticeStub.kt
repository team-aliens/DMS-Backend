package team.aliens.dms.domain.notice.stub

import team.aliens.dms.domain.notice.model.Notice
import java.time.LocalDateTime
import java.util.UUID

internal fun createNoticeStub(
    id: UUID = UUID.randomUUID(),
    managerId: UUID = UUID.randomUUID(),
    title: String = "title",
    content: String = "content",
    createdAt: LocalDateTime? = LocalDateTime.now(),
    updatedAt: LocalDateTime? = null
) = Notice(
    id = id,
    managerId = managerId,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt
)
