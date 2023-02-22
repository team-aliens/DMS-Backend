package team.aliens.dms.domain.point.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CreatePointOptionWebRequest(
    @field:NotBlank
    @field:Size(max = 20)
    val name: String?,

    @field:NotNull
    val score: Int?,

    @field:NotNull
    val type: WebPointType?
)
