package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveRemainOptionUseCase(
    private val userService: UserService,
    private val remainService: RemainService
) {

    fun execute(remainOptionId: UUID) {

        val user = userService.getCurrentUser()

        val remainOption = remainService.getRemainOptionById(remainOptionId, user.schoolId)

        remainService.deleteRemainOption(remainOption)
    }
}
