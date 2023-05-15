package team.aliens.dms.persistence.point.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.vo.PointHistoryVO
import java.time.LocalDateTime
import java.util.UUID

class QueryPointHistoryVO @QueryProjection constructor(
    id: UUID,
    date: LocalDateTime,
    pointType: PointType,
    pointName: String,
    pointScore: Int
) : PointHistoryVO(
    pointHistoryId = id,
    date = date.toLocalDate(),
    type = pointType,
    name = pointName,
    score = pointScore
)
