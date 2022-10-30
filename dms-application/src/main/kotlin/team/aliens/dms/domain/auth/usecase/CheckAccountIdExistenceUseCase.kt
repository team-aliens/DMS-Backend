package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.CoveredEmailPort

@ReadOnlyUseCase
class CheckAccountIdExistenceUseCase(
    private val queryUserPort: AuthQueryUserPort,
    private val coveredEmailPort: CoveredEmailPort
) {

    fun execute(accountId: String): String {
        val user = queryUserPort.queryUserByAccountId(accountId) ?: throw UserNotFoundException

        return coveredEmailPort.coveredEmail(user.email)
    }
}