package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomRequest
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class UpdateStudyRoomUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID, request: UpdateStudyRoomRequest) {

        val user = userService.getCurrentUser()
        val studyRoom = studyRoomService.getStudyRoom(studyRoomId, user.schoolId)

        if (request.floor != studyRoom.floor || request.name != studyRoom.name) {
            studyRoomService.checkStudyRoomExistsByFloorAndName(request.floor, request.name, user.schoolId)
        }
        studyRoomService.saveStudyRoom(
            request.toStudyRoom(studyRoom)
        )

        studyRoomService.updateTimeSlotsByStudyRoom(
            studyRoomId = studyRoom.id,
            studyRoomTimeSlots = request.toStudyRoomTimeSlots(studyRoom.id)
        )

        studyRoomService.updateSeatsByStudyRoom(
            studyRoomId = studyRoom.id,
            seats = request.toSeats(studyRoom.id)
        )
    }
}
