package team.aliens.dms.domain.student.dto.request

import team.aliens.dms.domain.manager.dto.request.Password
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class ResetStudentPasswordWebRequest(

    @field:NotBlank
    @field:Size(min = 4, max = 20)
    val accountId: String?,

    @field:NotBlank
    @field:Size(max = 10)
    val name: String?,

    @field:NotBlank
    @field:Size(max = 255)
    @field:Email
    val email: String?,

    @field:NotBlank
    @field:Size(min = 8, max = 8, message = "인증코드는 8글자 이여야 합니다.")
    val authCode: String?,

    @field:NotBlank
    @field:Size(min = 8, max = 20)
    @field:Pattern(
        regexp = Password.PATTERN,
        message = Password.MESSAGE
    )
    val newPassword: String?

)
