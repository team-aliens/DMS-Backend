package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.dto.ReissueResponse
import team.aliens.dms.domain.auth.service.AuthService
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class ReissueTokenUseCase(
    private val jwtPort: JwtPort,
    private val userService: UserService,
    private val schoolService: SchoolService,
    private val authService: AuthService
) {

    fun execute(token: String): ReissueResponse {
        val queryToken = authService.getRefreshTokenByToken(token)

        val (accessToken, accessTokenExpiredAt, refreshToken, refreshTokenExpiredAt) = jwtPort.receiveToken(
            userId = queryToken.userId, authority = queryToken.authority
        )

        val user = userService.queryUserById(queryToken.userId)

        val availableFeatures = schoolService.getAvailableFeaturesBySchoolId(user.schoolId)

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
