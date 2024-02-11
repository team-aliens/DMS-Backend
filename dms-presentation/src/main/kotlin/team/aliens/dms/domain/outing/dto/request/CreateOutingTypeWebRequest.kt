package team.aliens.dms.domain.outing.dto.request

import jakarta.validation.constraints.NotBlank

data class CreateOutingTypeWebRequest(
    @field:NotBlank
    val title: String
)
