package team.aliens.dms.domain.remain.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class RemainAvailableTime(

    val id: UUID,

    val startTime: LocalDate,

    val startDayOfWalk: DayOfWeek,

    val endTime: LocalDate,

    val endDayOfWalk: DayOfWeek

)