package team.aliens.dms.domain.daybreak.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateDaybreakStudyTypeWebRequest(
    @field:NotBlank
    @field:Size(max = 20)
    val name: String
)
