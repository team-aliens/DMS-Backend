package team.aliens.dms.domain.student.dto.request

import team.aliens.dms.domain.manager.dto.request.Password
import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class SignUpWebRequest(

    @field:NotBlank
    @field:Size(min = 8, max = 8, message = "학교 코드는 8글자 이여야 합니다")
    val schoolCode: String,

    @field:NotBlank
    @field:Size(max = 100)
    val schoolAnswer: String,

    @field:NotBlank
    @field:Email
    @field:Size(max = 255)
    val email: String,

    @field:NotBlank
    @field:Size(min = 6, max = 6, message = "인증 코드는 6글자 이여야 합니다")
    val authCode: String,

    @field:NotNull
    @field:Min(1)
    @field:Max(6)
    val grade: Int,

    @field:NotNull
    @field:Min(1)
    val classRoom: Int,

    @field:NotNull
    @field:Min(1)
    val number: Int,

    @field:NotBlank
    @field:Size(min = 4, max = 20)
    val accountId: String,

    @field:NotBlank
    @field:Pattern(
        regexp = Password.PATTERN,
        message = Password.MESSAGE
    )
    @field:Size(min = 8, max = 20)
    val password: String,

    @field:Size(max = 500)
    val profileImageUrl: String?

)
