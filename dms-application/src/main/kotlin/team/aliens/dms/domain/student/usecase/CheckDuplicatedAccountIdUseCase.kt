package team.aliens.dms.domain.student.usecase

import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException
import team.aliens.dms.global.annotation.ReadOnlyUseCase

@ReadOnlyUseCase
class CheckDuplicatedAccountIdUseCase(
    private val studentQueryUserPort: StudentQueryUserPort
) {

    fun execute(accountId: String) {
        if (studentQueryUserPort.existsByAccountId(accountId)) {
            throw UserAccountIdExistsException
        }
    }
}