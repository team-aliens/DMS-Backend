package team.aliens.dms.student.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class StudentPasswordInitializationWebRequest(

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
        regexp = "/^(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=-])(?=.*[0-9]).{8,25}$/",
        message = "영문, 숫자, 기호를 포함한 8~20자이어야 합니다."
    )
    val newPassword: String
)
