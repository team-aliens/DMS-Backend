package team.aliens.dms.domain.point.dto.request

import team.aliens.dms.domain.point.dto.PointRequestType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreatePointOptionWebRequest(
    @field:NotBlank
    val name: String?,

    @field:NotNull
    val score: Int?,

    @field:NotNull
    val type: WebPointType?
)
