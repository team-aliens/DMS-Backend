package team.aliens.dms.domain.notice.dto.request

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class UpdateNoticeWebRequest(

    @field:NotBlank
    @field:Length(max = 100)
    val title: String?,

    @field:NotBlank
    @field:Length(max = 1000)
    val content: String?

)
