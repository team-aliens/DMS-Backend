package team.aliens.dms.domain.remain.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

@Aggregate
data class RemainAvailableTime(

    val id: UUID,

    val startDayOfWeek: DayOfWeek,

    val startTime: LocalTime,

    val endDayOfWeek: DayOfWeek,

    val endTime: LocalTime,

)