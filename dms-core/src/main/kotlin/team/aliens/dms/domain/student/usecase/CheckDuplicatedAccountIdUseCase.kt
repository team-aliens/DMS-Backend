package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException

@ReadOnlyUseCase
class CheckDuplicatedAccountIdUseCase(
    private val queryUserPort: StudentQueryUserPort
) {

    fun execute(accountId: String) {
        if (queryUserPort.existsUserByAccountId(accountId)) {
            throw UserAccountIdExistsException
        }
    }
}
