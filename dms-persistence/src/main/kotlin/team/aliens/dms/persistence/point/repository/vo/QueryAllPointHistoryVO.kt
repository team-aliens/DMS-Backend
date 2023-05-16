package team.aliens.dms.persistence.point.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.vo.AllPointHistoryVO
import java.time.LocalDateTime
import java.util.UUID

class QueryAllPointHistoryVO @QueryProjection constructor(
    pointHistoryId: UUID,
    studentName: String,
    studentGcn: String,
    date: LocalDateTime,
    pointName: String,
    pointType: PointType,
    pointScore: Int
) : AllPointHistoryVO(
    pointHistoryId = pointHistoryId,
    studentName = studentName,
    studentGcn = studentGcn,
    date = date.toLocalDate(),
    pointName = pointName,
    pointType = pointType,
    pointScore = pointScore
)
