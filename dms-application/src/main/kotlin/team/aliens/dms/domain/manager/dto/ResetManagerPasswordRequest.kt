package team.aliens.dms.domain.manager.dto

data class ResetManagerPasswordRequest(

    val accountId: String,

    val email: String,

    val authCode: String,

    val newPassword: String
)
