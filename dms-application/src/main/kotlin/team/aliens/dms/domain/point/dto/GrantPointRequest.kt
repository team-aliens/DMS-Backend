package team.aliens.dms.domain.point.dto

import java.util.UUID

data class GrantPointRequest(
    val pointOptionId: UUID,
    val studentIdList: List<UUID>
)
