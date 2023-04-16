package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.exception.RemainAvailableTimeNotFoundException
import team.aliens.dms.domain.remain.exception.RemainCanNotAppliedException
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.model.RemainStatus
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class ApplyRemainUseCase(
    private val userService: UserService,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort,
    private val commandRemainStatusPort: CommandRemainStatusPort
) {

    fun execute(remainOptionId: UUID) {

        val user = userService.getCurrentUser()

        val remainOption = queryRemainOptionPort.queryRemainOptionById(remainOptionId)
            ?: throw RemainOptionNotFoundException

        validateSameSchool(remainOption.schoolId, user.schoolId)

        val remainAvailableTime = queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(user.schoolId)
            ?: throw RemainAvailableTimeNotFoundException

        if (!remainAvailableTime.isAvailable()) {
            throw RemainCanNotAppliedException
        }

        commandRemainStatusPort.saveRemainStatus(
            RemainStatus(
                id = user.id,
                remainOptionId = remainOption.id,
                createdAt = LocalDateTime.now()
            )
        )
    }
}
