package team.aliens.dms.domain.auth.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CertifyEmailWebRequest(

    @field:NotBlank
    @field:JsonProperty("account_id")
    val accountId: String,

    @field:Email
    @field:NotBlank
    val email: String

)
