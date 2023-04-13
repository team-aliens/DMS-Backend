package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomsResponse.StudyRoomElement
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@ReadOnlyUseCase
class ManagerQueryStudyRoomsUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(timeSlotId: UUID): ManagerQueryStudyRoomsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException

        validateSameSchool(timeSlot.schoolId, user.schoolId)

        val studyRooms = queryStudyRoomPort.queryAllStudyRoomsByTimeSlotId(timeSlotId).map {
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
