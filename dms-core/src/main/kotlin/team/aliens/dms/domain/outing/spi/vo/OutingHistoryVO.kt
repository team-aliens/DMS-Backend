package team.aliens.dms.domain.outing.spi.vo

import java.time.LocalTime
import java.util.UUID

open class OutingHistoryVO(
    val outingApplicationId: UUID,
    val name: String?,
    val outingType: String,
    val companionCount: Int,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime
)
