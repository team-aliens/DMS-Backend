package team.aliens.dms.domain.remain.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UpdateRemainOptionWebRequest(

    @field:NotBlank
    @field:Size(max = 100)
    val title: String?,

    @field:NotBlank
    @field:Size(max = 255)
    val description: String?
)
