package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.studyroom.exception.TimeSlotInUseException
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveTimeSlotUseCase(
    private val userService: UserService,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(timeSlotId: UUID) {

        val user = userService.getCurrentUser()
        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException
        validateSameSchool(user.schoolId, timeSlot.schoolId)

        if (queryStudyRoomPort.existsStudyRoomTimeSlotByTimeSlotId(timeSlotId)) {
            throw TimeSlotInUseException
        }

        commandStudyRoomPort.deleteSeatApplicationByTimeSlotId(timeSlotId)
        commandStudyRoomPort.deleteTimeSlotById(timeSlotId)
    }
}
