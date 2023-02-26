package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@UseCase
class RemoveStudyRoomUseCase(
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort
) {

    fun execute(studyRoomId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val studyRoom = queryStudyRoomPort.queryStudyRoomById(studyRoomId) ?: throw StudyRoomNotFoundException

        validateSameSchool(studyRoom.schoolId, manager.schoolId)

        commandStudyRoomPort.deleteAllSeatsByStudyRoomId(studyRoomId)
        commandStudyRoomPort.deleteStudyRoomById(studyRoomId)
    }
}
