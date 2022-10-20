package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.manager.dto.ResetPasswordManagerRequest
import team.aliens.dms.domain.manager.exception.AuthCodeNotMatchedException
import team.aliens.dms.domain.manager.exception.ManagerInfoNotMatchedException
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerQueryAuthCodePort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.global.annotation.UseCase

@UseCase
class ResetPasswordManagerUseCase(
    private val queryUserPort: ManagerQueryUserPort,
    private val queryAuthCodePort: ManagerQueryAuthCodePort,
    private val
) {

    fun execute(request: ResetPasswordManagerRequest) {
        val userByAccountId = queryUserPort.queryByAccountId(request.accountId) ?: throw ManagerNotFoundException

        val userByEmail = queryUserPort.queryByEmail(request.email) ?: throw ManagerNotFoundException

        if (userByAccountId.id != userByEmail.id) {
            throw ManagerInfoNotMatchedException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByUserId(userByEmail.id) ?: throw AuthCodeNotFoundException

        if (request.authCode != authCode.code) {
            throw AuthCodeNotMatchedException
        }


    }

}