package team.aliens.dms.domain.outing.dto.request

import java.time.DayOfWeek
import java.time.LocalTime

data class SetOutingAvailableTimeRequest(
    val dayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime
)
