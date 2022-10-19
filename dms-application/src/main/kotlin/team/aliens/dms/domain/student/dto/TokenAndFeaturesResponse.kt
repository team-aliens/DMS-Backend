package team.aliens.dms.domain.student.dto

import java.util.Date

data class TokenAndFeaturesResponse(
    val accessToken: String,
    val expiredAt: Date,
    val refreshToken: String,
    val features: Features
) {

    inner class Features(
        val noticeService: Boolean,
        val menuService: Boolean,
        val mealService: Boolean,
        val lostService: Boolean
    )
}
