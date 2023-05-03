package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.StudyRoomResponse
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class ManagerQueryStudyRoomUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID, timeSlotId: UUID): StudyRoomResponse {

        val user = userService.getCurrentUser()
        val studyRoom = studyRoomService.getStudyRoom(studyRoomId)

        val timeSlots = studyRoomService.getTimeSlots(
            schoolId = user.schoolId,
            studyRoomId = studyRoom.id
        ).apply {
            if (none { it.id == timeSlotId }) {
                throw StudyRoomTimeSlotNotFoundException
            }
        }

        val seats = studyRoomService.getSeatApplicationVOs(studyRoomId, timeSlotId)

        return StudyRoomResponse
            .StudyRoomResponseBuilder(studyRoom)
            .withStudyRoomDetail()
            .withTimeSlots(timeSlots)
            .withSeats(seats)
            .build()
    }
}
