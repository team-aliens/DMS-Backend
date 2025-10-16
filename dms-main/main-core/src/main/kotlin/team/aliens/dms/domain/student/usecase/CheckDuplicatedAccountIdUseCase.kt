package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class CheckDuplicatedAccountIdUseCase(
    private val userService: UserService
) {

    fun execute(accountId: String) {
        userService.checkUserNotExistsByAccountId(accountId)
    }
}
