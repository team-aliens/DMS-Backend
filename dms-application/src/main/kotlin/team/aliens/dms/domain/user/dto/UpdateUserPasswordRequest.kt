package team.aliens.dms.domain.user.dto

data class UpdateUserPasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
