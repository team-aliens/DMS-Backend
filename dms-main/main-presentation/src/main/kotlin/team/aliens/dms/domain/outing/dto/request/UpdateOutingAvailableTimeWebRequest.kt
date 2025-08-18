package team.aliens.dms.domain.outing.dto.request

import jakarta.validation.constraints.NotNull
import java.time.LocalTime

data class UpdateOutingAvailableTimeWebRequest(
    @field:NotNull
    val outingTime: LocalTime,

    @field:NotNull
    val arrivalTime: LocalTime
)
