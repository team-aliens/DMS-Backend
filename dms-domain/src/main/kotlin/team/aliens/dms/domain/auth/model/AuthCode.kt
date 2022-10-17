package team.aliens.dms.domain.auth.model

import team.aliens.dms.global.annotation.Aggregate
import java.util.*

@Aggregate
data class AuthCode(
    val code: String,

    val userId: UUID,

    val type: EmailType,

    val expirationTime: Int,
)
