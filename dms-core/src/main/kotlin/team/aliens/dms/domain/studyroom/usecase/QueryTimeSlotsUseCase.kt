package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryTimeSlotsUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {
    fun execute(): List<TimeSlot> {

        val user = userService.getCurrentUser()
        return studyRoomService.getTimeSlots(user.schoolId)
    }
}
