package team.aliens.dms.domain.auth.dto

import java.time.LocalDateTime

data class TokenResponse(

    val accessToken: String,

    val expiredAt: LocalDateTime,

    val refreshToken: String
)
