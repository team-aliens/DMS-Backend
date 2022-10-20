package team.aliens.dms.auth.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CertifyEmailWebRequest(

    @field:NotBlank
    val accountId: String,

    @field:Email
    @field:NotBlank
    val email: String
)
