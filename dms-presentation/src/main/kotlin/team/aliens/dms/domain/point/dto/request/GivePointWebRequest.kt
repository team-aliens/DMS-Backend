package team.aliens.dms.domain.point.dto.request

import org.jetbrains.annotations.NotNull
import java.util.*

data class GivePointWebRequest(
    @field:NotNull
    val pointOptionId: UUID?,
    @field:NotNull
    val studentIdList: List<UUID>?
)
