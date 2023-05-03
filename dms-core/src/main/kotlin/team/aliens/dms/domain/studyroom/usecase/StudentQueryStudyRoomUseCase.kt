package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.service.StudyRoomService

@ReadOnlyUseCase
class StudentQueryStudyRoomUseCase(
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID, timeSlotId: UUID): StudentQueryStudyRoomResponse {
        
        val studyRoom = studyRoomService.getStudyRoom(studyRoomId)
        val timeSlot = studyRoomService.getTimeSlot(timeSlotId)
        studyRoomService.checkStudyRoomTimeSlotExistsById(studyRoomId, timeSlotId)

        val seats = studyRoomService.getSeatApplicationVOs(studyRoomId, timeSlot.id)

        return StudentQueryStudyRoomResponse(
            studyRoom = studyRoom,
            timeSlot = timeSlot,
            seats = seats,
            studentId = studyRoomId
        )
    }
}
