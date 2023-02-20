package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@UseCase
class UpdateRemainOptionUseCase(
    private val securityPort: RemainSecurityPort,
    private val queryUserPort: RemainQueryUserPort,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val commendRemainOptionPort: CommandRemainOptionPort
) {
    fun execute(
        remainOptionId: UUID,
        title: String,
        description: String
    ) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val remainOption = queryRemainOptionPort.queryRemainOptionById(remainOptionId) ?: throw RemainOptionNotFoundException

        if (remainOption.schoolId != manager.schoolId) {
            throw SchoolMismatchException
        }

        commendRemainOptionPort.saveRemainOption(
            remainOption.copy(
                title = title,
                description = description
            )
        )
    }
}
