package team.aliens.dms.domain.point.dto.vo

import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDate
import java.util.UUID

data class QueryPointHistoryVO(
    val pointId: UUID,
    val date: LocalDate,
    val type: PointType,
    val name: String,
    val score: Int
)
