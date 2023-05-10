package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomSeatsRequest
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import java.util.UUID

@UseCase
class UpdateStudyRoomSeatsUseCase(
    private val studyRoomService: StudyRoomService
) {

    fun execute(request: UpdateStudyRoomSeatsRequest, studyRoomId: UUID) {
        val studyRoom = studyRoomService.getStudyRoom(studyRoomId)

        studyRoomService.saveStudyRoom(
            studyRoom.copy(
                availableHeadcount = request.availableHeadCount
            )
        )

        studyRoomService.updateSeatsByStudyRoom(
            studyRoomId = studyRoom.id,
            seats = request.toSeats(studyRoom.id)
        )
    }
}
