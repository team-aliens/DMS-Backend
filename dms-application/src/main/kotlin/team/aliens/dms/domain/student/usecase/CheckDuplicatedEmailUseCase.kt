package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserEmailExistsException

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
