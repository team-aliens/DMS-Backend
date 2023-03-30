package team.aliens.dms.domain.tag.stub

import team.aliens.dms.domain.tag.model.StudentTag
import java.time.LocalDateTime
import java.util.UUID

internal fun createStudentTagStub(
    studentId: UUID = UUID.randomUUID(),
    tagId: UUID = UUID.randomUUID(),
    createdAt: LocalDateTime? = LocalDateTime.now()
) = StudentTag(
    studentId = studentId,
    tagId = tagId,
    createdAt = createdAt
)
