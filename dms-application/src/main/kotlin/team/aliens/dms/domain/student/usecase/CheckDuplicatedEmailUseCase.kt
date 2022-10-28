package team.aliens.dms.domain.student.usecase

import team.aliens.dms.domain.user.exception.UserEmailExistsException
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.common.annotation.ReadOnlyUseCase

@ReadOnlyUseCase
class CheckDuplicatedEmailUseCase(
    private val queryUserPort: StudentQueryUserPort
) {

    fun execute(email: String) {
        if (queryUserPort.existsUserByEmail(email)) {
            throw UserEmailExistsException
        }
    }
}