package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException
import team.aliens.dms.domain.user.spi.QueryUserPort

@ReadOnlyUseCase
class CheckDuplicatedAccountIdUseCase(
    private val queryUserPort: QueryUserPort
) {

    fun execute(accountId: String) {
        if (queryUserPort.existsUserByAccountId(accountId)) {
            throw UserAccountIdExistsException
        }
    }
}
