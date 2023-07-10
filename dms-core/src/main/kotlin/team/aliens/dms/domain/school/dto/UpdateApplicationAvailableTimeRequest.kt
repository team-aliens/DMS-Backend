package team.aliens.dms.domain.school.dto

import team.aliens.dms.domain.school.model.ApplicationAvailableTimeType
import team.aliens.dms.domain.school.model.CustomDayOfWeek
import java.time.LocalTime

data class UpdateApplicationAvailableTimeRequest(

    val startDayOfWeek: CustomDayOfWeek,

    val startTime: LocalTime,

    val endDayOfWeek: CustomDayOfWeek,

    val endTime: LocalTime,

    val type: ApplicationAvailableTimeType
)