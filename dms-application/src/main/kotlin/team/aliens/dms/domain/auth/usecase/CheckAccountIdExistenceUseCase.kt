package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import team.aliens.dms.global.spi.CoveredEmailPort

@ReadOnlyUseCase
class CheckAccountIdExistenceUseCase(
    private val authQueryUserPort: AuthQueryUserPort,
    private val coveredEmailPort: CoveredEmailPort
) {

    fun execute(accountId: String): String {
        val user = authQueryUserPort.queryUserByAccountId(accountId) ?: throw UserNotFoundException

        return coveredEmailPort.coveredEmail(user.email)
    }
}