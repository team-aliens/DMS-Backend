package team.aliens.dms.domain.studyroom.dto

import java.time.LocalTime
import javax.validation.constraints.NotNull

data class UpdateAvailableTimeWebRequest(

    @field:NotNull
    val startAt: LocalTime,

    @field:NotNull
    val endAt: LocalTime

)
