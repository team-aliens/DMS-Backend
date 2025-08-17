package team.aliens.dms.domain.bug.stub

import team.aliens.dms.domain.bug.model.BugAttachment
import team.aliens.dms.domain.bug.model.BugReport
import team.aliens.dms.domain.bug.model.DevelopmentArea
import java.time.LocalDateTime
import java.util.UUID

internal fun createBugStub(
    id: UUID = UUID.randomUUID(),
    studentId: UUID = UUID.randomUUID(),
    content: String = "content",
    developmentArea: DevelopmentArea,
    createdAt: LocalDateTime? = LocalDateTime.now(),
    attachmentUrls: BugAttachment? = null
) = BugReport(
    id = id,
    studentId = studentId,
    content = content,
    developmentArea = developmentArea,
    createdAt = createdAt,
    attachmentUrls = attachmentUrls
)
