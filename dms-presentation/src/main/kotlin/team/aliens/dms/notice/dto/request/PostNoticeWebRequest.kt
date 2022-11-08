package team.aliens.dms.notice.dto.request

import javax.validation.constraints.NotBlank

data class PostNoticeWebRequest(

    @field:NotBlank
    val title: String,

    @field:NotBlank
    val content: String

)