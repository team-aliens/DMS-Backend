package team.aliens.dms.domain.outing.dto.request

import jakarta.validation.constraints.NotNull
import java.time.DayOfWeek
import java.time.LocalTime

data class SetOutingTimeWebRequest(
    @field:NotNull
    val dayOfWeek: DayOfWeek,

    @field:NotNull
    val startTime: LocalTime,

    @field:NotNull
    val endTime: LocalTime
)
