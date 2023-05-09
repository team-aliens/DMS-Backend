package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomRequest
import team.aliens.dms.domain.studyroom.dto.StudyRoomIdResponse
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class CreateStudyRoomUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(request: CreateStudyRoomRequest): StudyRoomIdResponse {

        val user = userService.getCurrentUser()

        val studyRoom = studyRoomService.createStudyRoom(
            request.toStudyRoom(schoolId = user.schoolId)
        )

        studyRoomService.createAllStudyRoomTimeSlots(
            request.toStudyRoomTimeSlots(studyRoomId = studyRoom.id)
        )

        studyRoomService.createAllSeats(
            request.toSeats(studyRoomId = studyRoom.id)
        )

        return StudyRoomIdResponse(studyRoom.id)
    }
}
