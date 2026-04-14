package team.aliens.dms.domain.daybreak.dto.request

import team.aliens.dms.domain.daybreak.model.Status
import java.util.UUID

data class ChangeDaybreakStudyApplicationStatusRequest(
    val applicationIds: List<UUID>,
    val status: Status
)
