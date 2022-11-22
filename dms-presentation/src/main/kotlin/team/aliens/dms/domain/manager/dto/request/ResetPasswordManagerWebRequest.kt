package team.aliens.dms.domain.manager.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class ResetPasswordManagerWebRequest(

    @field:NotBlank
    val accountId: String,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val authCode: String,

    @field:JsonFormat(shape = Shape.STRING)
    @field:NotBlank
    @field:Pattern(
        regexp = Password.PATTERN,
        message = Password.MESSAGE
    )
    val newPassword: Password

)
