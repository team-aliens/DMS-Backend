package team.aliens.dms.domain.remain.dto

import java.time.DayOfWeek
import java.time.LocalTime

data class UpdateRemainAvailableTimeRequest(

    val startDayOfWeek: DayOfWeek,

    val startTime: LocalTime,

    val endDayOfWeek: DayOfWeek,

    val endTime: LocalTime

)