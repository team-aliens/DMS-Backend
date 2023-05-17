package team.aliens.dms.domain.point.spi.vo

import team.aliens.dms.domain.point.model.PointType
import java.time.LocalDate
import java.util.UUID

open class AllPointHistoryVO(
    val pointHistoryId: UUID,
    val studentName: String,
    val studentGcn: String,
    val date: LocalDate,
    val pointName: String,
    val pointType: PointType,
    val pointScore: Int
)
