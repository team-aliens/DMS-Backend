package team.aliens.dms.domain.point.stub

import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import java.util.UUID

internal fun createPointOptionStub(
    id: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    name: String = "기숙사 봉사 활동",
    score: Int = 5,
    type: PointType = PointType.BONUS
) = PointOption(
    id = id,
    schoolId = schoolId,
    name = name,
    score = score,
    type = type
)