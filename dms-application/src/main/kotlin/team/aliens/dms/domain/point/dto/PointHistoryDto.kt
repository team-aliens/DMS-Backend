package team.aliens.dms.domain.point.dto

import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDate
import java.util.UUID

data class PointHistoryDto(
    val id: UUID,
    val date: LocalDate,
    val type: PointType,
    val name: String,
    val score: Int
)
