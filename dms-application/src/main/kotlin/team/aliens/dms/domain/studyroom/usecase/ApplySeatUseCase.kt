package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.studyroom.exception.SeatAlreadyAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.spi.CommandSeatPort
import team.aliens.dms.domain.studyroom.spi.QuerySeatPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@UseCase
class ApplySeatUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val querySeatPort: QuerySeatPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandSeatPort: CommandSeatPort
) {

    fun execute(seatId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val seat = querySeatPort.querySeatById(seatId) ?: throw SeatNotFoundException
        val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException

        if (user.schoolId != studyRoom.schoolId) {
            throw SchoolMismatchException
        }

        val saveSeat = seat.studentId?.run {
            throw SeatAlreadyAppliedException
        } ?: run {
            Seat(
                id = seat.id,
                studyRoomId = seat.studyRoomId,
                studentId = currentUserId,
                typeId = seat.typeId,
                widthLocation = seat.widthLocation,
                heightLocation = seat.heightLocation,
                number = seat.number,
                status = seat.status
            )
        }
        commandSeatPort.saveSeat(saveSeat)
    }
}