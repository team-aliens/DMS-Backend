package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.manager.dto.ResetManagerPasswordRequest
import team.aliens.dms.domain.auth.exception.AuthCodeNotMatchedException
import team.aliens.dms.domain.manager.exception.ManagerInfoNotMatchedException
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryAuthCodePort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.global.annotation.UseCase

@UseCase
class ResetManagerPasswordUseCase(
    private val queryUserPort: ManagerQueryUserPort,
    private val queryAuthCodePort: ManagerQueryAuthCodePort,
    private val commandUserPort: ManagerCommandUserPort,
    private val securityPort: ManagerSecurityPort
) {

    fun execute(request: ResetManagerPasswordRequest) {
        val user = queryUserPort.queryByAccountId(request.accountId) ?: throw ManagerNotFoundException

        if (user.email != request.email) {
            throw ManagerInfoNotMatchedException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmail(request.email) ?: throw AuthCodeNotFoundException

        if (request.authCode != authCode.code) {
            throw AuthCodeNotMatchedException
        }

        commandUserPort.saveUser(
            user.copy(password = securityPort.encode(request.newPassword))
        )
    }
}