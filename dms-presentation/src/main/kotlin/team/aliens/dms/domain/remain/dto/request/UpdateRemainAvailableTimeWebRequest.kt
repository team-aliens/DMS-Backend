package team.aliens.dms.domain.remain.dto.request

import java.time.DayOfWeek
import java.time.LocalTime
import javax.validation.constraints.NotNull

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
