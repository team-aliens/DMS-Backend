package team.aliens.dms.auth.dto.request

import javax.validation.constraints.NotBlank

data class SignInWebRequest(

    @field:NotBlank
    val accountId: String,

    @field:NotBlank
    val password: String

)