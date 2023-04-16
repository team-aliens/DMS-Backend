package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.SecurityService
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort
import team.aliens.dms.domain.manager.dto.ResetManagerPasswordRequest
import team.aliens.dms.domain.manager.exception.ManagerInfoMismatchException
import team.aliens.dms.domain.user.checkUserAuthority
import team.aliens.dms.domain.user.service.CommandUserService
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class ResetManagerPasswordUseCase(
    private val queryAuthCodePort: QueryAuthCodePort,
    private val commandUserService: CommandUserService,
    private val securityService: SecurityService,
    private val getUserService: GetUserService
) {

    fun execute(request: ResetManagerPasswordRequest) {

        val user = getUserService.queryUserByAccountId(request.accountId)
        checkUserAuthority(user.authority, Authority.MANAGER)

        if (user.email != request.email) {
            throw ManagerInfoMismatchException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmail(request.email) ?: throw AuthCodeNotFoundException

        authCode.validateAuthCode(request.authCode)

        commandUserService.saveUser(
            user.copy(password = securityService.encodePassword(request.newPassword))
        )
    }
}
