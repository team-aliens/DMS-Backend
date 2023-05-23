package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.service.StudyRoomService

@UseCase
class RemoveTimeSlotUseCase(
    private val studyRoomService: StudyRoomService
) {

    fun execute(timeSlotId: UUID) {
        val timeSlot = studyRoomService.getTimeSlot(timeSlotId)
        studyRoomService.deleteTimeSlot(timeSlot.id)
    }
}
