package team.aliens.dms.domain.remain.dto.request

import jakarta.validation.constraints.NotNull
import java.time.DayOfWeek
import java.time.LocalTime

data class UpdateRemainAvailableTimeWebRequest(

    @field:NotNull
    val startDayOfWeek: DayOfWeek,

    @field:NotNull
    val startTime: LocalTime,

    @field:NotNull
    val endDayOfWeek: DayOfWeek,

    @field:NotNull
    val endTime: LocalTime

)
