package team.aliens.dms.domain.studyroom.usecase

import java.time.LocalTime
import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.studyroom.exception.TimeSlotAlreadyExistsException
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class UpdateTimeSlotUseCase(
    private val getUserService: GetUserService,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(timeSlotId: UUID, startTime: LocalTime, endTime: LocalTime) {

        val user = getUserService.getCurrentUser()

        if (queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTime(startTime, endTime)) {
            throw TimeSlotAlreadyExistsException
        }

        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException
        validateSameSchool(user.schoolId, timeSlot.schoolId)

        commandStudyRoomPort.saveTimeSlot(
            timeSlot.copy(
                startTime = startTime,
                endTime = endTime
            )
        )
    }
}
