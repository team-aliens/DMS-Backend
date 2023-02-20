package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.dto.UpdateRemainAvailableTimeRequest
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.spi.CommandRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@UseCase
class UpdateRemainAvailableTimeUseCase(
    private val securityPort: RemainSecurityPort,
    private val queryUserPort: RemainQueryUserPort,
    private val commandRemainAvailableTimePort: CommandRemainAvailableTimePort,
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort
) {

    fun execute(request: UpdateRemainAvailableTimeRequest) {
        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val remainAvailableTime =
            queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(currentUser.schoolId)

        remainAvailableTime?.let {
            commandRemainAvailableTimePort.saveRemainAvailableTime(
                it.copy(
                    startDayOfWeek = it.startDayOfWeek,
                    startTime = it.startTime,
                    endDayOfWeek = it.endDayOfWeek,
                    endTime = it.endTime
                )
            )
        } ?: commandRemainAvailableTimePort.saveRemainAvailableTime(
            RemainAvailableTime(
                id = currentUser.schoolId,
                startDayOfWeek = request.startDayOfWeek,
                startTime = request.startTime,
                endDayOfWeek = request.endDayOfWeek,
                endTime = request.endTime
            )
        )
    }
}