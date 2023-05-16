package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.TimeSlotsResponse
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryTimeSlotsUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {
    fun execute(): TimeSlotsResponse {

        val user = userService.getCurrentUser()
        val timeSlots = studyRoomService.getTimeSlots(user.schoolId)

        return TimeSlotsResponse.of(timeSlots)
    }
}
