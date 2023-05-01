package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class ManagerQueryStudyRoomUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID, timeSlotId: UUID): Triple<StudyRoom, List<SeatApplicationVO>, List<TimeSlot>> {

        val user = userService.getCurrentUser()
        val studyRoom = studyRoomService.getStudyRoom(studyRoomId, user.schoolId)

        val timeSlots = studyRoomService.getTimeSlots(
            schoolId = user.schoolId,
            studyRoomId = studyRoom.id
        ).apply {
            if (none { it.id == timeSlotId }) {
                throw StudyRoomTimeSlotNotFoundException
            }
        }

        val seats = studyRoomService.getSeatApplicationVOs(studyRoomId, timeSlotId)

        return Triple(studyRoom, seats, timeSlots)
    }
}
