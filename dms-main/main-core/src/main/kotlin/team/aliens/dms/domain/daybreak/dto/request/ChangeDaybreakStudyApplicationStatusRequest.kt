package team.aliens.dms.domain.daybreak.dto.request

import team.aliens.dms.domain.daybreak.model.Status
import java.util.UUID

data class ChangeDaybreakStudyApplicationStatusRequest(
    val applicationIdList: List<UUID>,
    val status: Status
)
