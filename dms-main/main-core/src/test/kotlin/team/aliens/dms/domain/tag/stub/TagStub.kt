package team.aliens.dms.domain.tag.stub

import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

internal fun createTagStub(
    id: UUID = UUID.randomUUID(),
    name: String = "소프트웨어개발과",
    color: String = "#FF4646",
    schoolId: UUID = UUID.randomUUID()
) = Tag(
    id = id,
    name = name,
    color = color,
    schoolId = schoolId
)
