package team.aliens.dms.domain.point.stub

import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDateTime
import java.util.UUID

internal fun createPointHistoryStub(
    id: UUID = UUID.randomUUID(),
    studentName: String = "이름",
    studentGcn: String = "2201",
    bonusTotal: Int = 5,
    minusTotal: Int = 0,
    isCancel: Boolean = false,
    pointName: String = "기숙사 봉사 활동",
    pointScore: Int = 3,
    pointType: PointType = PointType.BONUS,
    createdAt: LocalDateTime = LocalDateTime.now(),
    schoolId: UUID = UUID.randomUUID()
) = PointHistory(
    id = id,
    studentName = studentName,
    studentGcn = studentGcn,
    bonusTotal = bonusTotal,
    minusTotal = minusTotal,
    isCancel = isCancel,
    pointName = pointName,
    pointScore = pointScore,
    pointType = pointType,
    createdAt = createdAt,
    schoolId = schoolId
)

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

internal fun createPhraseStub(
    id: UUID = UUID.randomUUID(),
    content: String = "내용",
    type: PointType = PointType.BONUS,
    standard: Int = 0
) = Phrase(
    id = id,
    content = content,
    type = type,
    standard = standard
)
