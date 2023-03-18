package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import java.util.UUID

@UseCase
class UnApplySeatUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(timeSlotId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        commandStudyRoomPort.deleteSeatApplicationByStudentIdAndTimeSlotId(currentUserId, timeSlotId)
    }
}
