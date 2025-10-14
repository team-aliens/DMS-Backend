package team.aliens.dms.domain.auth.model

import java.util.UUID

data class UserAuthInfo(
    val userId: UUID,
    val schoolId: UUID,
    val authority: Authority
)
