package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomRequest
import team.aliens.dms.domain.studyroom.dto.StudyRoomIdResponse
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class CreateStudyRoomUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(request: CreateStudyRoomRequest): StudyRoomIdResponse {

        val user = userService.getCurrentUser()

        studyRoomService.checkStudyRoomExistsByFloorAndName(request.floor, request.name, user.schoolId)
        val studyRoom = studyRoomService.saveStudyRoom(
            request.toStudyRoom(schoolId = user.schoolId)
        )

        studyRoomService.saveAllStudyRoomTimeSlots(
            request.toStudyRoomTimeSlots(studyRoomId = studyRoom.id)
        )

        studyRoomService.saveAllSeats(
            request.toSeats(studyRoomId = studyRoom.id)
        )

        return StudyRoomIdResponse(studyRoom.id)
    }
}
