package team.aliens.dms.persistence.point.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDateTime

data class QueryPointHistoryVO @QueryProjection constructor(
    val date: LocalDateTime,
    val type: PointType,
    val name: String,
    val score: Int
)
