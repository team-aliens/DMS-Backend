package team.aliens.dms.domain.tag.dto

import jakarta.validation.constraints.NotNull
import team.aliens.dms.common.validator.NotNullElements
import java.util.UUID

data class GrantTagWebRequest(

    @field:NotNull
    val tagId: UUID,

    @field:NotNullElements
    val studentIds: List<UUID>

)
