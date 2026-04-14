package team.aliens.dms.domain.daybreak.dto

import jakarta.validation.constraints.NotNull
import team.aliens.dms.domain.daybreak.model.Status
import java.util.UUID

data class ChangeDaybreakStudyApplicationStatusWebRequest(

    @field:NotNull
    val applicationIds: List<UUID>,

    @field:NotNull
    val status: Status
)
