package team.aliens.dms.domain.outing.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateOutingTypeWebRequest(
    @field:Size(max = 20)
    @field:NotBlank
    val title: String
)
