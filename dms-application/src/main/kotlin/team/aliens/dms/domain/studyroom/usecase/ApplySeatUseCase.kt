package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
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

        val saveSeat = seat.run {
            Seat(
                id = id,
                studyRoomId = studyRoomId,
                studentId = currentUserId,
                typeId = typeId,
                widthLocation = widthLocation,
                heightLocation = heightLocation,
                number = number,
                status = status
            )
        }
        commandSeatPort.saveSeat(saveSeat)
    }
}