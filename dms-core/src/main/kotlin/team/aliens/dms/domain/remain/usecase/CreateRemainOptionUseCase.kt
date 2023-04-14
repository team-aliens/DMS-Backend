package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class CreateRemainOptionUseCase(
    private val getUserService: GetUserService,
    private val commentRemainOptionPort: CommandRemainOptionPort
) {

    fun execute(title: String, description: String): UUID {

        val user = getUserService.getCurrentUser()

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
