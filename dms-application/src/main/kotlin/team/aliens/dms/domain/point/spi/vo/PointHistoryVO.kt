package team.aliens.dms.domain.point.spi.vo

import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDate

data class PointHistoryVO(
    val date: LocalDate,
    val type: PointType,
    val name: String,
    val score: Int
)
