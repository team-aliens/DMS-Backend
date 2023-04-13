package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.auth.dto.SignInRequest
import team.aliens.dms.domain.auth.dto.SignInResponse
import team.aliens.dms.domain.auth.exception.PasswordMismatchException
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@UseCase
class SignInUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val querySchoolPort: QuerySchoolPort,
    private val jwtPort: JwtPort
) {

    fun execute(request: SignInRequest): SignInResponse {
        val user = queryUserPort.queryUserByAccountId(request.accountId) ?: throw UserNotFoundException

        if (!securityPort.isPasswordMatch(request.password, user.password)) {
            throw PasswordMismatchException
        }

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
