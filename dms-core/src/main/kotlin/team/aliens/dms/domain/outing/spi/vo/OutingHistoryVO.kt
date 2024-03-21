package team.aliens.dms.domain.outing.spi.vo

import team.aliens.dms.domain.outing.model.OutingStatus
import java.time.LocalTime
import java.util.UUID

open class OutingHistoryVO(
    val outingApplicationId: UUID,
    val studentName: String,
    val outingType: String,
    val outingCompanionCount: Int,
    val outingStatus: OutingStatus,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime
)
