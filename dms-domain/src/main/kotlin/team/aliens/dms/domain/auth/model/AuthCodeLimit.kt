package team.aliens.dms.domain.auth.model

import team.aliens.dms.global.annotation.Aggregate
import java.util.*

@Aggregate
data class AuthCodeLimit(
    val id: UUID,

    val userId: UUID,

    val type: EmailType,

    val attemptCount: Int,

    val isVerified: Boolean,

    val expirationTime: Int
)
