package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@UseCase
class CreateRemainOptionUseCase(
    private val securityPort: RemainSecurityPort,
    private val queryUserPort: QueryUserPort,
    private val commentRemainOptionPort: CommandRemainOptionPort
) {

    fun execute(title: String, description: String): UUID {

        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val savedRemainOption = commentRemainOptionPort.saveRemainOption(
            RemainOption(
                schoolId = currentUser.schoolId,
                title = title,
                description = description
            )
        )

        return savedRemainOption.id
    }
}
