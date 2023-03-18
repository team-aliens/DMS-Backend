package team.aliens.dms.domain.remain.stub

import team.aliens.dms.domain.remain.model.RemainOption
import java.util.UUID

internal fun createRemainOption(
    id: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    title: String = "title",
    description: String = "description"
) = RemainOption(
    id = id,
    schoolId = schoolId,
    title = title,
    description = description
)