package team.aliens.dms.domain.notice.dto.request

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UpdateNoticeWebRequest(

    @field:NotBlank
    @field:Size(max = 100)
    val title: String?,

    @field:NotBlank
    @field:Size(max = 1000)
    val content: String?

)
