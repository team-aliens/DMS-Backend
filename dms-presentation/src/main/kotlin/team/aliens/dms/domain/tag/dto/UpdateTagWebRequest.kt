package team.aliens.dms.domain.tag.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UpdateTagWebRequest(
    @field:NotBlank
    @field:Size(max = 10)
    val name: String,

    @field:NotBlank
    @field:Size(min = 7, max = 7)
    val color: String
)
