package team.aliens.dms.domain.auth.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignInWebRequest(

    @field:NotBlank
    @field:Size(min = 4, max = 20)
    val accountId: String,

    @field:NotBlank
    @field:Size(min = 8, max = 20)
    val password: String,

    @field:NotBlank
    val deviceToken: String

)
