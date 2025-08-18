package team.aliens.dms.domain.point.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UpdatePointOptionWebRequest(
    @field:NotBlank
    @field:Size(max = 20)
    val name: String,

    @field:NotNull
    val score: Int
)
