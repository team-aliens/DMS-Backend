package team.aliens.dms.domain.daybreak.dto.request

import java.util.UUID

data class RevertDaybreakStudyApplicationRequest(
    val applicationIdList: List<UUID>
)
