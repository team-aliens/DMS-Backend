package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.studyroom.exception.SeatAlreadyAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatCanNotAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@UseCase
class ApplySeatUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(seatId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val seat = queryStudyRoomPort.querySeatById(seatId) ?: throw SeatNotFoundException
        val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException

        if (user.schoolId != studyRoom.schoolId) {
            throw SchoolMismatchException
        }

        if (seat.status != SeatStatus.AVAILABLE) {
            throw SeatCanNotAppliedException
        }

        val saveSeat = seat.studentId?.run {
            throw SeatAlreadyAppliedException
        } ?: run {
            seat.copy(studentId = currentUserId)
        }
        commandStudyRoomPort.saveSeat(saveSeat)
        commandStudyRoomPort.saveStudyRoom(
            studyRoom.plusInUseHeadcount()
        )
    }
}