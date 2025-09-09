package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.dto.TokenFeatureResponse
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

    fun execute(token: String): TokenFeatureResponse {
        val queryToken = authService.getRefreshTokenByToken(token)

        val user = userService.queryUserById(queryToken.userId)

        val tokenResponse = jwtPort.receiveToken(
            userId = queryToken.userId, authority = queryToken.authority, schoolId = user.schoolId
        )

        val availableFeatures = schoolService.getAvailableFeaturesBySchoolId(user.schoolId)

        return TokenFeatureResponse.of(tokenResponse, availableFeatures)
    }
}
