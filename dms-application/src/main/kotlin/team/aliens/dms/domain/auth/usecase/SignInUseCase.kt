package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.dto.SignInRequest
import team.aliens.dms.domain.auth.dto.SignInResponse
import team.aliens.dms.domain.auth.exception.PasswordMismatchException
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.auth.spi.AuthSecurityPort
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.CheckUserAuthority

@UseCase
class SignInUseCase(
    private val securityPort: AuthSecurityPort,
    private val queryUserPort: AuthQueryUserPort,
    private val jwtPort: JwtPort,
    private val checkUserAuthority: CheckUserAuthority
) {

    fun execute(request: SignInRequest): SignInResponse {
        val user = queryUserPort.queryUserByAccountId(request.accountId) ?: throw UserNotFoundException

        if (!securityPort.isPasswordMatch(request.password, user.password)) {
            throw PasswordMismatchException
        }

        val authority = checkUserAuthority.execute(user.id)

        val (accessToken, expiredAt, refreshToken) = jwtPort.receiveToken(user.id, authority)

        return SignInResponse(
            accessToken = accessToken,
            expiredAt = expiredAt,
            refreshToken = refreshToken,
            features = SignInResponse.Features(
                // TODO 서비스 관리 테이블 필요
                mealService = true,
                noticeService = true,
                pointService = true
            )
        )
    }
}