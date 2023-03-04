package team.aliens.dms.domain.auth.dto

import team.aliens.dms.domain.auth.model.Authority
import java.time.LocalDateTime

data class ReissueResponse(
    val accessToken: String,
    val accessTokenExpiredAt: LocalDateTime,
    val refreshToken: String,
    val refreshTokenExpiredAt: LocalDateTime,
    val authority: Authority,
    val features: Features
) {
    data class Features(
        val mealService: Boolean,
        val noticeService: Boolean,
        val pointService: Boolean,
        val studyRoomService: Boolean
    )
}
