package team.aliens.dms.domain.point.dto.request

import java.util.*
import javax.validation.constraints.NotNull

data class GrantPointWebRequest(
    @field:NotNull
    val pointOptionId: UUID?,

    @field:NotNull
    val studentIdList: List<UUID>?
)
