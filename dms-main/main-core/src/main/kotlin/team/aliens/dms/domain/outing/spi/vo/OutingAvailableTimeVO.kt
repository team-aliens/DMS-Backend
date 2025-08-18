package team.aliens.dms.domain.outing.spi.vo

import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

open class OutingAvailableTimeVO(
    val outingAvailableTimeId: UUID,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val enabled: Boolean,
    val dayOfWeek: DayOfWeek
)
