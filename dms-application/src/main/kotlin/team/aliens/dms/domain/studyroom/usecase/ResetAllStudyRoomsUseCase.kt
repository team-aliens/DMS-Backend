package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort

@UseCase
class ResetAllStudyRoomsUseCase(
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute() {
        commandStudyRoomPort.deleteAllSeatApplications()
    }
}
