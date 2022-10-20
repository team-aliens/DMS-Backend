package team.aliens.dms.domain.student.dto

data class ResetPasswordStudentRequest(

    val accountId: String,

    val name: String,

    val email: String,

    val authCode: String,

    val newPassword: String
)
