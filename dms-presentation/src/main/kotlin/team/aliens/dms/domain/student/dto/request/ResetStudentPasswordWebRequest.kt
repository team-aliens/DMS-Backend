package team.aliens.dms.domain.student.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import team.aliens.dms.domain.manager.dto.request.PasswordRegex

data class ResetStudentPasswordWebRequest(

    @field:NotBlank
    @field:Size(min = 4, max = 20)
    val accountId: String,

    @field:NotBlank
    @field:Size(max = 10)
    val name: String,

    @field:NotBlank
    @field:Size(max = 255)
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Size(min = 6, max = 6, message = "인증코드는 8글자 이여야 합니다.")
    val authCode: String,

    @field:NotBlank
    @field:Size(min = 8, max = 20)
    @field:Pattern(
        regexp = PasswordRegex.PATTERN,
        message = PasswordRegex.MESSAGE
    )
    val newPassword: String

)
