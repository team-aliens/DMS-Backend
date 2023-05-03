package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.StudyRoomsResponse
import team.aliens.dms.domain.studyroom.service.StudyRoomService

@ReadOnlyUseCase
class ManagerQueryStudyRoomsUseCase(
    private val studyRoomService: StudyRoomService
) {

    fun execute(timeSlotId: UUID): StudyRoomsResponse {

        val timeSlot = studyRoomService.getTimeSlot(timeSlotId)
        val studyRooms = studyRoomService.getStudyRoomVOs(timeSlot.id)

        return StudyRoomsResponse.of(studyRooms)
    }
}
