package team.aliens.dms.domain.auth.dto

import java.time.LocalDateTime

data class ReissueTokenResponse(
    val accessToken: String,
    val expiredAt: LocalDateTime,
    val refreshToken: String,
    val features: Features
) {
    data class Features(
        val mealService: Boolean,
        val noticeService: Boolean,
        val pointService: Boolean
    )
}