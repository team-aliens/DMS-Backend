package team.aliens.dms.persistence.point.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class QueryPointHistoryVO @QueryProjection constructor(
    val pointId: UUID,
    val date: LocalDateTime,
    val type: PointType,
    val name: String,
    val score: Int
)
