package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.domain.auth.dto.ReissueResponse
import team.aliens.dms.domain.auth.exception.RefreshTokenNotFoundException
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.spi.QueryRefreshTokenPort
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.dto.SignInResponse
import team.aliens.dms.domain.auth.spi.AuthQuerySchoolPort
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.user.exception.UserNotFoundException

@UseCase
class ReissueTokenUseCase(
    private val jwtPort: JwtPort,
    private val queryRefreshTokenPort: QueryRefreshTokenPort,
    private val queryUserPort: AuthQueryUserPort,
    private val querySchoolPort: AuthQuerySchoolPort
) {

    fun execute(token: String): ReissueResponse {
        val queryToken = queryRefreshTokenPort.queryRefreshTokenByToken(token) ?: throw RefreshTokenNotFoundException

        val (accessToken, accessTokenExpiredAt, refreshToken, refreshTokenExpiredAt) = jwtPort.receiveToken(queryToken.userId, queryToken.authority)

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
                    pointService = pointService
                )
            }
        )
    }
}