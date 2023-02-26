package team.aliens.dms.domain.auth.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class SendEmailCodeWebRequest(

    @field:NotBlank
    @field:Size(max = 255)
    @field:Email
    val email: String?,

    @field:NotNull
    val type: WebEmailType?

)
