package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort

@ReadOnlyUseCase
class StudentQueryStudyRoomUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(studyRoomId: UUID): StudentQueryStudyRoomResponse {
        val currentUserId = securityPort.getCurrentUserId()

        val studyRoom = queryStudyRoomPort.queryStudyRoomById(studyRoomId) ?: throw StudyRoomNotFoundException
        val seatCount = queryStudyRoomPort.countSeatByStudyRoomIdAndStatus(studyRoomId, SeatStatus.AVAILABLE)

        val seats = queryStudyRoomPort.queryAllSeatByStudyRoomId(studyRoomId).map {
            it.copy(
                isMine = isMine(
                    studentId = it.student?.id,
                    currentUserId = currentUserId
                )
            )
        }

        return studyRoom.run {
            StudentQueryStudyRoomResponse(
                totalAvailableSeat = seatCount,
                inUseHeadcount = inUseHeadcount!!,
                availableSex = availableSex,
                availableGrade = availableGrade,
                eastDescription = eastDescription,
                westDescription = westDescription,
                southDescription = southDescription,
                northDescription = northDescription,
                totalWidthSize = widthSize,
                totalHeightSize = heightSize,
                seats = seats
            )
        }
    }

    private fun isMine(studentId: UUID?, currentUserId: UUID) = studentId?.run {
        studentId == currentUserId
    }
}