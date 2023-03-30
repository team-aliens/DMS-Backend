package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort

@SchedulerUseCase
class ResetAllStudyRoomsUseCase(
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute() {
        commandStudyRoomPort.deleteAllSeatApplications()
    }
}
