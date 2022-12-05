package team.aliens.dms.domain.student.dto.request

import team.aliens.dms.domain.manager.dto.request.Password
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class ResetStudentPasswordWebRequest(

    @field:NotBlank
    val accountId: String,

    @field:NotBlank
    val name: String,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val authCode: String,

    @field:NotBlank
    @field:Pattern(
        regexp = Password.PATTERN,
        message = Password.MESSAGE
    )
    val newPassword: String

)
