package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.service.AuthService
import team.aliens.dms.domain.manager.dto.ResetManagerPasswordRequest
import team.aliens.dms.domain.manager.exception.ManagerInfoMismatchException
import team.aliens.dms.domain.user.checkUserAuthority
import team.aliens.dms.domain.user.service.UserService

@UseCase
class ResetManagerPasswordUseCase(
    private val authService: AuthService,
    private val userService: UserService,
    private val securityService: SecurityService
) {

    fun execute(request: ResetManagerPasswordRequest) {

        val user = userService.queryUserByAccountId(request.accountId)
        checkUserAuthority(user.authority, Authority.MANAGER)

        if (user.email != request.email) {
            throw ManagerInfoMismatchException
        }

        authService.checkAuthCodeByEmail(user.email, request.authCode)

        userService.saveUser(
            user.copy(password = securityService.encodePassword(request.newPassword))
        )
    }
}
