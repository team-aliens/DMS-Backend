package team.aliens.dms.domain.user.dto

data class UpdateUserPasswordRequest(
    val currentPassword: String,
    val newPassword: String
)
