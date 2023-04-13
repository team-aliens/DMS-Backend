package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.dto.ReissueResponse
import team.aliens.dms.domain.auth.exception.RefreshTokenNotFoundException
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.spi.QueryRefreshTokenPort
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@UseCase
class ReissueTokenUseCase(
    private val jwtPort: JwtPort,
    private val queryRefreshTokenPort: QueryRefreshTokenPort,
    private val queryUserPort: QueryUserPort,
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(token: String): ReissueResponse {
        val queryToken = queryRefreshTokenPort.queryRefreshTokenByToken(token) ?: throw RefreshTokenNotFoundException

        val (accessToken, accessTokenExpiredAt, refreshToken, refreshTokenExpiredAt) = jwtPort.receiveToken(
            userId = queryToken.userId, authority = queryToken.authority
        )

        val user = queryUserPort.queryUserById(queryToken.userId) ?: throw UserNotFoundException

        val availableFeatures = querySchoolPort.queryAvailableFeaturesBySchoolId(user.schoolId)
            ?: throw FeatureNotFoundException

        return ReissueResponse(
            accessToken = accessToken,
            accessTokenExpiredAt = accessTokenExpiredAt,
            refreshToken = refreshToken,
            refreshTokenExpiredAt = refreshTokenExpiredAt,
            features = availableFeatures.run {
                ReissueResponse.Features(
                    mealService = mealService,
                    noticeService = noticeService,
                    pointService = pointService,
                    studyRoomService = studyRoomService,
                    remainService = remainService
                )
            }
        )
    }
}
