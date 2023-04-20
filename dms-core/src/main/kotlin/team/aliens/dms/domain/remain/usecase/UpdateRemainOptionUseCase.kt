package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.service.GetRemainService
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class UpdateRemainOptionUseCase(
    private val userService: UserService,
    private val remainService: GetRemainService,
    private val commendRemainOptionPort: CommandRemainOptionPort
) {

    fun execute(remainOptionId: UUID, title: String, description: String) {

        val user = userService.getCurrentUser()
        val remainOption = remainService.getRemainOptionById(remainOptionId, user.schoolId)

        commendRemainOptionPort.saveRemainOption(
            remainOption.copy(
                title = title,
                description = description
            )
        )
    }
}
