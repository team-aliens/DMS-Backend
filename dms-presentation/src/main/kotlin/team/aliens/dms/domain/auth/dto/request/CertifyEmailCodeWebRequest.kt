package team.aliens.dms.domain.auth.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CertifyEmailCodeWebRequest(

    @field:Email
    @field:NotBlank
    val email: String,

    @field:Length(min = 6, max = 6)
    @field:NotBlank
    @field:JsonProperty("auth_code")
    val authCode: String,

    @field:NotNull
    val type: EmailType

)
