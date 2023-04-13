package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@UseCase
class RemoveRemainOptionUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val commandRemainOptionPort: CommandRemainOptionPort,
    private val commandRemainStatusPort: CommandRemainStatusPort
) {

    fun execute(remainOptionId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val remainOption = queryRemainOptionPort.queryRemainOptionById(remainOptionId)
            ?: throw RemainOptionNotFoundException

        validateSameSchool(remainOption.schoolId, manager.schoolId)

        commandRemainStatusPort.deleteRemainStatusByRemainOptionId(remainOption.id)
        commandRemainOptionPort.deleteRemainOption(remainOption)
    }
}
