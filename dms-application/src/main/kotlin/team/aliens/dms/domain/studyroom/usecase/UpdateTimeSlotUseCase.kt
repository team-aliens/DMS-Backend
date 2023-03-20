package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalTime
import java.util.UUID

@UseCase
class UpdateTimeSlotUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {
    fun execute(timeSlotId: UUID, startTime: LocalTime, endTime: LocalTime) {
        val currentManagerId = securityPort.getCurrentUserId()
        val currentManager = queryUserPort.queryUserById(currentManagerId) ?: throw UserNotFoundException

        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException
        validateSameSchool(currentManager.schoolId, timeSlot.schoolId)

        commandStudyRoomPort.saveTimeSlot(
            timeSlot.copy(
                startTime = startTime,
                endTime = endTime
            )
        )
    }
}
