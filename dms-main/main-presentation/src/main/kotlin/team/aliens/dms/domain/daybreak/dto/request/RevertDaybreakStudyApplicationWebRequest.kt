package team.aliens.dms.domain.daybreak.dto.request

import jakarta.validation.constraints.NotEmpty
import java.util.UUID

data class RevertDaybreakStudyApplicationWebRequest(

    @field:NotEmpty
    val applicationIdList: List<UUID>
)
