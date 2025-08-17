package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class CreateRemainOptionUseCase(
    private val userService: UserService,
    private val commentRemainOptionPort: CommandRemainOptionPort
) {

    fun execute(title: String, description: String): UUID {

        val user = userService.getCurrentUser()

        val savedRemainOption = commentRemainOptionPort.saveRemainOption(
            RemainOption(
                schoolId = user.schoolId,
                title = title,
                description = description
            )
        )

        return savedRemainOption.id
    }
}
