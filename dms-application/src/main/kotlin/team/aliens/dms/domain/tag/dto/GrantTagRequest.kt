package team.aliens.dms.domain.tag.dto

import java.util.UUID

data class GrantTagRequest(

    val tagId: UUID,

    val studentIds: List<UUID>

)
