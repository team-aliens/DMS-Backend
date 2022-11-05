package team.aliens.dms.notice.dto.request

import javax.validation.constraints.NotBlank

data class UpdateNoticeWebRequest(

    @field:NotBlank
    val title: String,

    @field:NotBlank
    val content: String
)
