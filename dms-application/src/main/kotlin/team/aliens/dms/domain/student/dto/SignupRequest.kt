package team.aliens.dms.domain.student.dto

data class SignupRequest(
    val schoolCode: String,
    val schoolAnswer: String,
    val authCode: String,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val accountId: String,
    val password: String,
    val email: String,
    val profileImageUrl: String?
)