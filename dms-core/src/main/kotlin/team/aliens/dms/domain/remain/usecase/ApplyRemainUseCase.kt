package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.remain.exception.RemainAvailableTimeNotFoundException
import team.aliens.dms.domain.remain.exception.RemainCanNotAppliedException
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.model.RemainStatus
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class ApplyRemainUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryRemainOptionPort: QueryRemainOptionPort,
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort,
    private val commandRemainStatusPort: CommandRemainStatusPort
) {

    fun execute(remainOptionId: UUID) {

        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val remainOption = queryRemainOptionPort.queryRemainOptionById(remainOptionId)
            ?: throw RemainOptionNotFoundException

        validateSameSchool(remainOption.schoolId, currentUser.schoolId)

        val remainAvailableTime = queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(currentUser.schoolId)
            ?: throw RemainAvailableTimeNotFoundException

        if (!remainAvailableTime.isAvailable()) {
            throw RemainCanNotAppliedException
        }

        commandRemainStatusPort.saveRemainStatus(
            RemainStatus(
                id = currentUser.id,
                remainOptionId = remainOption.id,
                createdAt = LocalDateTime.now()
            )
        )
    }
}
