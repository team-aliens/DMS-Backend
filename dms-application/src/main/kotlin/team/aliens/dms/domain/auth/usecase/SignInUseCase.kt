package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.domain.auth.dto.SignInRequest
import team.aliens.dms.domain.auth.dto.TokenAndFeaturesResponse
import team.aliens.dms.domain.auth.exception.PasswordMismatchException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.AuthQueryStudentPort
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.spi.SecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.global.annotation.UseCase

@UseCase
class SignInUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: AuthQueryUserPort,
    private val queryStudentPort: AuthQueryStudentPort,
    private val jwtPort: JwtPort
) {

    fun execute(request: SignInRequest): TokenAndFeaturesResponse {

        val user =
            queryUserPort.queryUserByAccountId(request.accountId) ?: throw UserNotFoundException
        var authority = Authority.STUDENT

        if (!securityPort.isPasswordMatch(request.password, user.password)) {
            throw PasswordMismatchException
        }

        if (queryStudentPort.queryStudentById(user.id) == null) {
            authority = Authority.MANAGER
        }

        val tokenResponse = jwtPort.receiveToken(user.id, authority)

        return TokenAndFeaturesResponse(
            accessToken = tokenResponse.accessToken,
            expiredAt = tokenResponse.expiredAt,
            refreshToken = tokenResponse.refreshToken,
            features = TokenAndFeaturesResponse.Features(
                mealService = true,
                noticeService = true,
                pointService = true
            )
        )
    }
}