package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.ManagerQueryStudyRoomsResponse.StudyRoomElement
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@ReadOnlyUseCase
class ManagerQueryStudyRoomsUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(timeSlotId: UUID?): ManagerQueryStudyRoomsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        timeSlotId?.run {
            if (!queryStudyRoomPort.existsTimeSlotById(timeSlotId)) {
                throw StudyRoomTimeSlotNotFoundException
            }
        } ?: run {
            if (queryStudyRoomPort.existsTimeSlotsBySchoolId(user.schoolId)) {
                throw StudyRoomTimeSlotNotFoundException
            }
        }

        val studyRooms = queryStudyRoomPort.queryAllStudyRoomVOsByTimeSlotId(timeSlotId).map {
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
