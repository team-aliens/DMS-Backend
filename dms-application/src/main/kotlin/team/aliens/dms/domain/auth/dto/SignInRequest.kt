package team.aliens.dms.domain.auth.dto

data class SignInRequest(
    val accountId: String,
    val password: String,
    val authority: String
)
