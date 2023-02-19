package team.aliens.dms.domain.point.dto.request

import java.util.*
import javax.validation.constraints.NotNull

data class GranatPointWebRequest(
    @field:NotNull
    val pointOptionId: UUID?,

    @field:NotNull
    val studentIdList: List<UUID>?
)
