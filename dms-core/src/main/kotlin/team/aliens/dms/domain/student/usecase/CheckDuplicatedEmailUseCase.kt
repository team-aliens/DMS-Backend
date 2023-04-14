package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class CheckDuplicatedEmailUseCase(
    private val getUserService: GetUserService
) {

    fun execute(email: String) {
        getUserService.checkUserExistsByEmail(email)
    }
}
