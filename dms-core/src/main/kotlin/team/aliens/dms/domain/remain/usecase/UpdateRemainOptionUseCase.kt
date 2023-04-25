package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class UpdateRemainOptionUseCase(
    private val userService: UserService,
    private val remainService: RemainService
) {

    fun execute(remainOptionId: UUID, title: String, description: String) {

        val user = userService.getCurrentUser()
        val remainOption = remainService.getRemainOptionById(remainOptionId, user.schoolId)

        remainService.saveRemainOption(
            remainOption.copy(
                title = title,
                description = description
            )
        )
    }
}
