package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class CheckAccountIdExistenceUseCase(
    private val queryUserPort: AuthQueryUserPort
) {

    fun execute(accountId: String): String {
        val user = queryUserPort.queryUserByAccountId(accountId) ?: throw UserNotFoundException

        return StringUtil.coveredEmail(user.email)
    }
}
