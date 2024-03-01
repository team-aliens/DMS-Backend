package team.aliens.dms.domain.studyroom.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalTime

data class UpdateAvailableTimeWebRequest(

    @field:NotNull
    val startAt: LocalTime,

    @field:NotNull
    val endAt: LocalTime

)
