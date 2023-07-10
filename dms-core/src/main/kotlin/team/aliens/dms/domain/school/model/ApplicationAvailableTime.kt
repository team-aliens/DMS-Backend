package team.aliens.dms.domain.school.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

@Aggregate
data class ApplicationAvailableTime(

    val type: ApplicationAvailableTimeType,

    val schoolId: UUID,

    val startDayOfWeek: CustomDayOfWeek,

    val startTime: LocalTime,

    val endDayOfWeek: CustomDayOfWeek,

    val endTime: LocalTime,
)
