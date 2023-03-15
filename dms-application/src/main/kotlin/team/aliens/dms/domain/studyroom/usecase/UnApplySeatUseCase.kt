package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.StudyRoomFacade
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@UseCase
class UnApplySeatUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val studyRoomFacade: StudyRoomFacade,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(timeSlotId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        studyRoomFacade.validateNullableTimeSlotId(timeSlotId, user.schoolId)

        commandStudyRoomPort.deleteSeatApplicationByStudentIdAndTimeSlotId(currentUserId, timeSlotId)
    }
}
