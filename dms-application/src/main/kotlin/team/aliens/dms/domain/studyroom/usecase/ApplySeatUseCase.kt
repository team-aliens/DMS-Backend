package team.aliens.dms.domain.studyroom.usecase

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
import java.time.LocalTime
import java.util.UUID

@UseCase
class ApplySeatUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val unApplySeatUseCase: UnApplySeatUseCase,
    private val queryAvailableTimeUseCase: QueryAvailableTimeUseCase
) {

    fun execute(seatId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val seat = queryStudyRoomPort.querySeatById(seatId) ?: throw SeatNotFoundException
        val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException

        if (user.schoolId != studyRoom.schoolId) {
            throw SchoolMismatchException
        }

        val now = LocalTime.now()
        val availableTime = queryAvailableTimeUseCase.execute()

        if (now < availableTime.startAt || now > availableTime.endAt) {
            throw SeatCanNotAppliedException
        }

        if (seat.status != SeatStatus.AVAILABLE) {
            throw SeatCanNotAppliedException
        }

        val currentSeat = queryStudyRoomPort.querySeatByStudentId(currentUserId)
        currentSeat?.let {
            unApplySeatUseCase.execute()
        }

        val saveSeat = seat.studentId?.run {
            throw SeatAlreadyAppliedException
        } ?: run {
            seat.use(currentUserId)
        }
        commandStudyRoomPort.saveSeat(saveSeat)

        commandStudyRoomPort.saveStudyRoom(
            studyRoom.apply()
        )
    }
}