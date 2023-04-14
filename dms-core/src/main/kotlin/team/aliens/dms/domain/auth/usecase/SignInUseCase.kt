package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.SecurityService
import team.aliens.dms.domain.auth.dto.SignInRequest
import team.aliens.dms.domain.auth.dto.SignInResponse
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class SignInUseCase(
    private val securityService: SecurityService,
    private val getUserService: GetUserService,
    private val querySchoolPort: QuerySchoolPort,
    private val jwtPort: JwtPort
) {

    fun execute(request: SignInRequest): SignInResponse {
        val user = getUserService.queryUserByAccountId(request.accountId)

        securityService.checkIsPasswordMatches(request.password, user.password)

        val (accessToken, accessTokenExpiredAt, refreshToken, refreshTokenExpiredAt) = jwtPort.receiveToken(
            userId = user.id, authority = user.authority
        )

        val availableFeatures = querySchoolPort.queryAvailableFeaturesBySchoolId(user.schoolId)
            ?: throw FeatureNotFoundException

        return SignInResponse(
            accessToken = accessToken,
            accessTokenExpiredAt = accessTokenExpiredAt,
            refreshToken = refreshToken,
            refreshTokenExpiredAt = refreshTokenExpiredAt,
            features = availableFeatures.run {
                SignInResponse.Features(
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
