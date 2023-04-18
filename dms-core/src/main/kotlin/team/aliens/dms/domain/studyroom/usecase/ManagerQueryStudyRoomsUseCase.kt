package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomsResponse.StudyRoomElement
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class ManagerQueryStudyRoomsUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(timeSlotId: UUID): ManagerQueryStudyRoomsResponse {

        val user = userService.getCurrentUser()
        val timeSlot = studyRoomService.getTimeSlot(timeSlotId, user.schoolId)

        val studyRooms = studyRoomService.getStudyRoomVOs(timeSlot.id).map {
            StudyRoomElement(
                id = it.id,
                floor = it.floor,
                name = it.name,
                availableGrade = it.availableGrade,
                availableSex = it.availableSex,
                inUseHeadcount = it.inUseHeadcount,
                totalAvailableSeat = it.totalAvailableSeat
            )
        }

        return ManagerQueryStudyRoomsResponse(
            studyRooms = studyRooms
        )
    }
}
