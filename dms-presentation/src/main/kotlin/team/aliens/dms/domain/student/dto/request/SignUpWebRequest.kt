package team.aliens.dms.domain.student.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.validator.constraints.Length
import team.aliens.dms.domain.manager.dto.request.Password
import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class SignUpWebRequest(

    @field:NotBlank
    @field:Length(min = 8, max = 8)
    val schoolCode: String,

    @field:NotBlank
    @field:Length(max = 100)
    val schoolAnswer: String,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Length(min = 6, max = 6)
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
    @field:Length(max = 20)
    val accountId: String,

    @field:JsonFormat(shape = JsonFormat.Shape.STRING)
    @field:NotBlank
    @field:Pattern(
        regexp = Password.PATTERN,
        message = Password.MESSAGE
    )
    val password: Password,

    val profileImageUrl: String?

)