package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.manager.dto.ResetManagerPasswordRequest
import team.aliens.dms.domain.manager.exception.ManagerInfoMismatchException
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryAuthCodePort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.CheckUserAuthority

@UseCase
class ResetManagerPasswordUseCase(
    private val queryUserPort: ManagerQueryUserPort,
    private val queryAuthCodePort: ManagerQueryAuthCodePort,
    private val commandUserPort: ManagerCommandUserPort,
    private val securityPort: ManagerSecurityPort,
    private val checkUserAuthority: CheckUserAuthority
) {

    fun execute(request: ResetManagerPasswordRequest) {
        val user = queryUserPort.queryUserByAccountId(request.accountId) ?: throw UserNotFoundException

        if (checkUserAuthority.execute(user.id) != Authority.MANAGER) {
            throw ManagerNotFoundException
        }

        if (user.email != request.email) {
            throw ManagerInfoMismatchException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmail(request.email) ?: throw AuthCodeNotFoundException

        if (authCode.code != request.authCode) {
            throw AuthCodeMismatchException
        }

        commandUserPort.saveUser(
            user.copy(password = securityPort.encodePassword(request.newPassword))
        )
    }
}