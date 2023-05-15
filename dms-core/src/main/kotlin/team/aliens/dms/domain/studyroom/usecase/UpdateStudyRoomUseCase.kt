package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomRequest
import team.aliens.dms.domain.studyroom.service.StudyRoomService

@UseCase
class UpdateStudyRoomUseCase(
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID, request: UpdateStudyRoomRequest) {

        studyRoomService.updateStudyRoom(
            studyRoom = studyRoomService.getStudyRoom(studyRoomId),
            updateFunction = request.toStudyRoomUpdateFunction()
        )

        studyRoomService.updateTimeSlotsByStudyRoom(
            studyRoomId = studyRoomId,
            studyRoomTimeSlots = request.toStudyRoomTimeSlots(studyRoomId)
        )

        studyRoomService.updateSeatsByStudyRoom(
            studyRoomId = studyRoomId,
            seats = request.toSeats(studyRoomId)
        )
    }
}
