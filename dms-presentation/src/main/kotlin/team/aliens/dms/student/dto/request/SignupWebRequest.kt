package team.aliens.dms.student.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignupWebRequest(

    @field:NotBlank
    val schoolCode: String,

    @field:NotBlank
    val schoolAnswer: String,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val authCode: String,

    @field:NotBlank
    val grade: Int,

    @field:NotBlank
    val classRoom: Int,

    @field:NotBlank
    val number: Int,

    @field:NotBlank
    val accountId: String,

    @field:NotBlank
    val password: String,

    val profileImageUrl: String?
)