package team.aliens.dms.domain.studyroom.usecase

import java.time.LocalTime
import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.exception.TimeSlotAlreadyExistsException
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class CreateTimeSlotUseCase(
    private val getUserService: GetUserService,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(startTime: LocalTime, endTime: LocalTime): UUID {

        val user = getUserService.getCurrentUser()

        if (queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTime(startTime, endTime)) {
            throw TimeSlotAlreadyExistsException
        }

        val savedTimeSlot = commandStudyRoomPort.saveTimeSlot(
            TimeSlot(
                schoolId = user.schoolId,
                startTime = startTime,
                endTime = endTime
            )
        )

        return savedTimeSlot.id
    }
}
