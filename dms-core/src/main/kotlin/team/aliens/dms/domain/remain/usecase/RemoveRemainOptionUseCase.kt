package team.aliens.dms.domain.remain.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class RemoveRemainOptionUseCase(
    private val getUserService: GetUserService,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val commandRemainOptionPort: CommandRemainOptionPort,
    private val commandRemainStatusPort: CommandRemainStatusPort
) {

    fun execute(remainOptionId: UUID) {

        val user = getUserService.getCurrentUser()

        val remainOption = queryRemainOptionPort.queryRemainOptionById(remainOptionId)
            ?: throw RemainOptionNotFoundException

        validateSameSchool(remainOption.schoolId, user.schoolId)

        commandRemainStatusPort.deleteRemainStatusByRemainOptionId(remainOption.id)
        commandRemainOptionPort.deleteRemainOption(remainOption)
    }
}
