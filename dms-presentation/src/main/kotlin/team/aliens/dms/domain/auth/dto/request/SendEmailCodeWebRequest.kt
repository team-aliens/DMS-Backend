package team.aliens.dms.domain.auth.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class SendEmailCodeWebRequest(

    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotNull
    val type: EmailType?

)