package team.aliens.dms.domain.point.dto.request

import team.aliens.dms.common.validator.NotNullElements
import java.util.UUID
import javax.validation.constraints.NotNull

data class GrantPointWebRequest(
    @field:NotNull
    val pointOptionId: UUID?,

    @field:NotNullElements
    val studentIdList: List<UUID>?
)
