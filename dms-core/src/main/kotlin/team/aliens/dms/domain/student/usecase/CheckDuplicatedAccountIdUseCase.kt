package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class CheckDuplicatedAccountIdUseCase(
    private val getUserService: GetUserService
) {

    fun execute(accountId: String) {
        getUserService.checkUserExistsByAccountId(accountId)
    }
}
