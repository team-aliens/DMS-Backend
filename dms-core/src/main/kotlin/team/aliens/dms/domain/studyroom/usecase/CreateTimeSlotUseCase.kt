package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.exception.TimeSlotAlreadyExistsException
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalTime
import java.util.UUID

@UseCase
class CreateTimeSlotUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(startTime: LocalTime, endTime: LocalTime): UUID {
        val currentManagerId = securityPort.getCurrentUserId()
        val currentManager = queryUserPort.queryUserById(currentManagerId) ?: throw UserNotFoundException

        if (queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTime(startTime, endTime)) {
            throw TimeSlotAlreadyExistsException
        }

        val savedTimeSlot = commandStudyRoomPort.saveTimeSlot(
            TimeSlot(
                schoolId = currentManager.schoolId,
                startTime = startTime,
                endTime = endTime
            )
        )

        return savedTimeSlot.id
    }
}
