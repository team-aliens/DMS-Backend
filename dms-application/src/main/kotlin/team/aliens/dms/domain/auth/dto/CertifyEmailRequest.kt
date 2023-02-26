package team.aliens.dms.domain.auth.dto

data class CertifyEmailRequest(
    val accountId: String,
    val email: String
)
