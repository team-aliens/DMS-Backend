package team.aliens.dms.domain.studyroom.dto

import java.time.LocalTime
import javax.validation.constraints.NotNull

data class UpdateTimeSlotWebRequest(

    @field:NotNull
    val startTime: LocalTime?,

    @field:NotNull
    val endTime: LocalTime?
)
