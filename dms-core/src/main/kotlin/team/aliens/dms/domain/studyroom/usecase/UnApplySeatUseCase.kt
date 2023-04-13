package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import java.util.UUID

@UseCase
class UnApplySeatUseCase(
    private val securityPort: SecurityPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(seatId: UUID, timeSlotId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        commandStudyRoomPort.deleteSeatApplicationBySeatIdAndStudentIdAndTimeSlotId(seatId, currentUserId, timeSlotId)
    }
}
