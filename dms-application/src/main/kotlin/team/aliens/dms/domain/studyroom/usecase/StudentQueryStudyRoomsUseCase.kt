package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.StudyRoomFacade
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse.StudyRoomElement
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@ReadOnlyUseCase
class StudentQueryStudyRoomsUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val studyRoomFacade: StudyRoomFacade
) {

    fun execute(timeSlotId: UUID?): StudentQueryStudyRoomsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        studyRoomFacade.validateNullableTimeSlotId(timeSlotId, user.schoolId)

        val seatApplication = queryStudyRoomPort.querySeatApplicationByStudentId(currentUserId)
        val userStudyRoomId = seatApplication?.let { queryStudyRoomPort.querySeatById(it.seatId)?.studyRoomId }

        val studyRooms = queryStudyRoomPort.queryAllStudyRoomsByTimeSlotId(timeSlotId)
            .map {
                StudyRoomElement(
                    id = it.id,
                    floor = it.floor,
                    name = it.name,
                    availableGrade = it.availableGrade,
                    availableSex = it.availableSex,
                    inUseHeadcount = it.inUseHeadcount,
                    totalAvailableSeat = it.totalAvailableSeat,
                    isMine = isMine(
                        userStudyRoomId = userStudyRoomId,
                        studyRoomId = it.id
                    )
                )
            }

        return StudentQueryStudyRoomsResponse(
            studyRooms = studyRooms
        )
    }

    private fun isMine(userStudyRoomId: UUID?, studyRoomId: UUID) = userStudyRoomId?.run {
        this == studyRoomId
    } ?: run {
        false
    }
}
