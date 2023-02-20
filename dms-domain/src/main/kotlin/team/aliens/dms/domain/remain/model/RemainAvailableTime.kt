package team.aliens.dms.domain.remain.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

@Aggregate
data class RemainAvailableTime(

    val id: UUID,

    val startTime: LocalTime,

    val startDayOfWeek: DayOfWeek,

    val endTime: LocalTime,

    val endDayOfWeek: DayOfWeek

)