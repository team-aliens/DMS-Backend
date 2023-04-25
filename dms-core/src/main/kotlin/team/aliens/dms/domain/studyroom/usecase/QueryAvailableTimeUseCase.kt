package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.QueryAvailableTimeResponse
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryAvailableTimeUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(): QueryAvailableTimeResponse {

        val user = userService.getCurrentUser()
        val availableTime = studyRoomService.getAvailableTime(user.schoolId)

        return QueryAvailableTimeResponse(
            startAt = availableTime.startAt,
            endAt = availableTime.endAt
        )
    }
}
