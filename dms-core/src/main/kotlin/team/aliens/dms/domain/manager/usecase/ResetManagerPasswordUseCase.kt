package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort
import team.aliens.dms.domain.manager.dto.ResetManagerPasswordRequest
import team.aliens.dms.domain.manager.exception.ManagerInfoMismatchException
import team.aliens.dms.domain.user.exception.InvalidRoleException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.CheckUserAuthority
import team.aliens.dms.domain.user.spi.CommandUserPort
import team.aliens.dms.domain.user.spi.QueryUserPort

@UseCase
class ResetManagerPasswordUseCase(
    private val queryUserPort: QueryUserPort,
    private val queryAuthCodePort: QueryAuthCodePort,
    private val commandUserPort: CommandUserPort,
    private val securityPort: SecurityPort,
    private val checkUserAuthority: CheckUserAuthority
) {

    fun execute(request: ResetManagerPasswordRequest) {
        val user = queryUserPort.queryUserByAccountId(request.accountId) ?: throw UserNotFoundException

        if (checkUserAuthority.execute(user.id) != Authority.MANAGER) {
            throw InvalidRoleException
        }

        if (user.email != request.email) {
            throw ManagerInfoMismatchException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmail(request.email) ?: throw AuthCodeNotFoundException

        authCode.validateAuthCode(request.authCode)

        commandUserPort.saveUser(
            user.copy(password = securityPort.encodePassword(request.newPassword))
        )
    }
}
