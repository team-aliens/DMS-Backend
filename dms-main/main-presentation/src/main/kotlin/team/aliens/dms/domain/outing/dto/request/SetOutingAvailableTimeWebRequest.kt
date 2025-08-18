package team.aliens.dms.domain.outing.dto.request

import jakarta.validation.constraints.NotNull
import java.time.DayOfWeek
import java.time.LocalTime

data class SetOutingAvailableTimeWebRequest(
    @field:NotNull
    val dayOfWeek: DayOfWeek,

    @field:NotNull
    val outingTime: LocalTime,

    @field:NotNull
    val arrivalTime: LocalTime
)
