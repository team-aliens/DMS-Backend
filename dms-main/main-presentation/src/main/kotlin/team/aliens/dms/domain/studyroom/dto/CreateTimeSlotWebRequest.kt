package team.aliens.dms.domain.studyroom.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalTime

data class CreateTimeSlotWebRequest(

    @field:NotNull
    val startTime: LocalTime,

    @field:NotNull
    val endTime: LocalTime
)
