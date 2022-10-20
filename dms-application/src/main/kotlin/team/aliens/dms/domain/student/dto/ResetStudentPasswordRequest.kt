package team.aliens.dms.domain.student.dto

data class ResetStudentPasswordRequest(

    val accountId: String,

    val name: String,

    val email: String,

    val authCode: String,

    val newPassword: String
)
