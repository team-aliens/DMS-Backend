package team.aliens.dms.domain.school.dto.request

import team.aliens.dms.domain.school.model.ApplicationAvailableTimeType
import team.aliens.dms.domain.school.model.CustomDayOfWeek
import java.time.LocalTime
import javax.validation.constraints.NotNull

data class UpdateApplicationAvailableTimeWebRequest(

    val startDayOfWeek: CustomDayOfWeek? = CustomDayOfWeek.NONE,

    @field:NotNull
    val startTime: LocalTime,

    val endDayOfWeek: CustomDayOfWeek? = CustomDayOfWeek.NONE,

    @field:NotNull
    val endTime: LocalTime,

    @field:NotNull
    val type: ApplicationAvailableTimeType
)
