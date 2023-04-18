package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveTimeSlotUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(timeSlotId: UUID) {

        val user = userService.getCurrentUser()

        val timeSlot = studyRoomService.getTimeSlot(timeSlotId, user.schoolId)
        studyRoomService.deleteTimeSlot(timeSlot.id)
    }
}
