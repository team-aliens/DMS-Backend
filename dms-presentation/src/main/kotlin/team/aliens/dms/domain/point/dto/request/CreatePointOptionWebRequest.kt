package team.aliens.dms.domain.point.dto.request

import team.aliens.dms.domain.point.dto.PointRequestType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.math.max

data class CreatePointOptionWebRequest(
    @field:NotBlank
    @field:Size(max = 20)
    val name: String?,

    @field:NotNull
    val score: Int?,

    @field:NotNull
    val type: WebPointType?
)
