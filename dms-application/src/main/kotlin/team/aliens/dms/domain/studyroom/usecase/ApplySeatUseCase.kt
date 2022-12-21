package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.exception.SeatAlreadyAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.spi.CommandSeatPort
import team.aliens.dms.domain.studyroom.spi.QuerySeatPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort

@UseCase
class ApplySeatUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val querySeatPort: QuerySeatPort,
    private val commandSeatPort: CommandSeatPort
) {

    fun execute(seatId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()

        val seat = querySeatPort.querySeatById(seatId) ?: throw SeatNotFoundException

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