package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.RemainAvailableTimeResponse
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryRemainAvailableTimeUseCase(
    private val userService: UserService,
    private val remainService: RemainService
) {

    fun execute(): RemainAvailableTimeResponse {

        val user = userService.getCurrentUser()

        val availableTime = remainService.getRemainAvailableTimeBySchoolId(user.schoolId)

        return RemainAvailableTimeResponse(
            startDayOfWeek = availableTime.startDayOfWeek,
            startTime = availableTime.startTime,
            endDayOfWeek = availableTime.endDayOfWeek,
            endTime = availableTime.endTime
        )
    }
}
