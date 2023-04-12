package team.aliens.dms.persistence.point.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDateTime
import java.util.UUID

data class QueryAllPointHistoryVO @QueryProjection constructor(
    val pointHistoryId: UUID,
    val studentName: String,
    val studentGcn: String,
    val date: LocalDateTime,
    val pointName: String,
    val pointType: PointType,
    val pointScore: Int
)
