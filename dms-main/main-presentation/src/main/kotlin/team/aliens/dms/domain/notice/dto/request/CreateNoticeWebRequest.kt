package team.aliens.dms.domain.notice.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateNoticeWebRequest(

    @field:NotBlank
    @field:Size(max = 100)
    val title: String,

    @field:NotBlank
    @field:Size(max = 1000)
    val content: String

)
