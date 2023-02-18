package team.aliens.dms.domain.point.dto

import java.util.UUID

data class GivePointRequest(
    val pointOptionId: UUID,
    val studentIdList: List<UUID>
)
