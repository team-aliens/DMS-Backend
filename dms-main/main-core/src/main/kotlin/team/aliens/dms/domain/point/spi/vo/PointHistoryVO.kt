package team.aliens.dms.domain.point.spi.vo

import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDate
import java.util.UUID

open class PointHistoryVO(
    val pointHistoryId: UUID,
    val date: LocalDate,
    val type: PointType,
    val name: String,
    val score: Int
)
