package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class UnApplySeatUseCase(
    private val userService: UserService,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute(seatId: UUID, timeSlotId: UUID) {
        val student = userService.getCurrentStudent()
        commandStudyRoomPort.deleteSeatApplicationBySeatIdAndStudentIdAndTimeSlotId(seatId, student.id, timeSlotId)
    }
}
