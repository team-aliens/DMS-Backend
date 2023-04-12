package team.aliens.dms.domain.tag.dto

import team.aliens.dms.common.validator.NotNullElements
import java.util.UUID
import javax.validation.constraints.NotNull

data class GrantTagWebRequest(

    @field:NotNull
    val tagId: UUID,

    @field:NotNullElements
    val studentIds: List<UUID>

)
