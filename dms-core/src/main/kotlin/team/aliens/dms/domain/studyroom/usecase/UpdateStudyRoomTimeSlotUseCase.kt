package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomTimeSlotRequest
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import java.util.UUID

@UseCase
class UpdateStudyRoomTimeSlotUseCase(
    private val studyRoomService: StudyRoomService
) {

    fun execute(request: UpdateStudyRoomTimeSlotRequest, studyRoomId: UUID) {
        val studyRoom = studyRoomService.getStudyRoom(studyRoomId)

        studyRoomService.updateTimeSlotsByStudyRoom(
            studyRoomId = studyRoomId,
            studyRoomTimeSlots = request.toStudyRoomTimeSlots(studyRoom.id)
        )
    }
}
