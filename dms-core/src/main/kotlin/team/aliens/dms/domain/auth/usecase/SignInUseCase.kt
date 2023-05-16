package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.auth.dto.SignInRequest
import team.aliens.dms.domain.auth.dto.TokenFeatureResponse
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class SignInUseCase(
    private val securityService: SecurityService,
    private val userService: UserService,
    private val schoolService: SchoolService,
    private val jwtPort: JwtPort
) {

    fun execute(request: SignInRequest): TokenFeatureResponse {
        val user = userService.queryUserByAccountId(request.accountId)

        securityService.checkIsPasswordMatches(request.password, user.password)

        val tokenResponse = jwtPort.receiveToken(
            userId = user.id, authority = user.authority
        )
        val availableFeatures = schoolService.getAvailableFeaturesBySchoolId(user.schoolId)

        return TokenFeatureResponse.of(tokenResponse, availableFeatures)
    }
}
