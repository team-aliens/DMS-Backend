package team.aliens.dms.domain.auth.dto

import team.aliens.dms.domain.school.model.AvailableFeature
import java.time.LocalDateTime

data class TokenFeatureResponse(
    val accessToken: String,
    val accessTokenExpiredAt: LocalDateTime,
    val refreshToken: String,
    val refreshTokenExpiredAt: LocalDateTime,
    val features: Features
) {
    data class Features(
        val mealService: Boolean,
        val noticeService: Boolean,
        val pointService: Boolean,
        val studyRoomService: Boolean,
        val remainService: Boolean
    )

    companion object {
        fun of(tokenResponse: TokenResponse, availableFeatures: AvailableFeature) =
            TokenFeatureResponse(
                accessToken = tokenResponse.accessToken,
                accessTokenExpiredAt = tokenResponse.accessTokenExpiredAt,
                refreshToken = tokenResponse.refreshToken,
                refreshTokenExpiredAt = tokenResponse.refreshTokenExpiredAt,
                features = Features(
                    mealService = availableFeatures.mealService,
                    noticeService = availableFeatures.noticeService,
                    pointService = availableFeatures.pointService,
                    studyRoomService = availableFeatures.studyRoomService,
                    remainService = availableFeatures.remainService
                )
            )
    }
}
