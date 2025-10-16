package team.aliens.dms.domain.point.dto.request

import jakarta.validation.constraints.NotNull
import team.aliens.dms.common.validator.NotNullElements
import java.util.UUID

data class GrantPointWebRequest(
    @field:NotNull
    val pointOptionId: UUID,

    @field:NotNullElements
    val studentIdList: List<UUID>
)
