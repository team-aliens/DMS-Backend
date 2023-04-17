package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.dto.UpdateRemainAvailableTimeRequest
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.spi.CommandRemainAvailableTimePort
import team.aliens.dms.domain.user.service.UserService

@UseCase
class UpdateRemainAvailableTimeUseCase(
    private val userService: UserService,
    private val commandRemainAvailableTimePort: CommandRemainAvailableTimePort
) {

    fun execute(request: UpdateRemainAvailableTimeRequest) {

        val user = userService.getCurrentUser()

        commandRemainAvailableTimePort.saveRemainAvailableTime(
            RemainAvailableTime(
                id = user.schoolId,
                startDayOfWeek = request.startDayOfWeek,
                startTime = request.startTime,
                endDayOfWeek = request.endDayOfWeek,
                endTime = request.endTime
            )
        )
    }
}
