package team.aliens.dms.domain.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class SendEmailCodeWebRequest(

    @field:NotBlank
    @field:Size(max = 255)
    @field:Email
    val email: String,

    @field:NotNull
    val type: WebEmailType

)
