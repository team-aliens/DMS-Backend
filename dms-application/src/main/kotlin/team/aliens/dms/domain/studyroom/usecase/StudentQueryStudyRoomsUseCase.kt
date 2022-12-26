package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse.StudyRoomElement
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class StudentQueryStudyRoomsUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(): StudentQueryStudyRoomsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val studyRoom = queryStudyRoomPort.queryAllStudyRoomsBySchoolId(user.schoolId).map {
            StudyRoomElement(
                id = it.id,
                floor = it.floor,
                name = it.name,
                availableGrade = it.availableGrade,
                availableSex = it.availableSex,
                inUseHeadcount = it.inUseHeadcount!!,
                totalAvailableSeat = queryStudyRoomPort.countSeatByStudyRoomId(it.id),
                isMine = isMine(
                    studyRoomId = it.id,
                    currentUserId = currentUserId
                )
            )
        }

        return StudentQueryStudyRoomsResponse(
            studyRooms = studyRoom
        )
    }

    private fun isMine(studyRoomId: UUID, currentUserId: UUID) : Boolean {
        val studentId = queryStudyRoomPort.querySeatByStudyRoomId(studyRoomId)?.studentId

        return studentId == currentUserId
    }
}