package team.aliens.dms.domain.daybreak.dto.request

import jakarta.validation.constraints.NotNull
import team.aliens.dms.domain.daybreak.model.Status
import java.util.UUID

data class ChangeDaybreakStudyApplicationStatusWebRequest(

    @field:NotNull
    val applicationIdList: List<UUID>,

    @field:NotNull
    val status: Status
)
