package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.StudentQueryMyStudyRoomResponse
import team.aliens.dms.domain.studyroom.exception.AppliedSeatNotFound
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort

@ReadOnlyUseCase
class StudentQueryMyStudyRoomUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(): StudentQueryMyStudyRoomResponse {
        val currentUserId = securityPort.getCurrentUserId()

        val seat = queryStudyRoomPort.querySeatByStudentId(currentUserId)?: throw AppliedSeatNotFound
        val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException

        return StudentQueryMyStudyRoomResponse(
            floor = studyRoom.floor,
            name = studyRoom.name
        )
    }
}