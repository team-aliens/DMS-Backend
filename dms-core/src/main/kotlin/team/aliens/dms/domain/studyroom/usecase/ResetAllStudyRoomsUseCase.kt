package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.studyroom.service.StudyRoomService

@SchedulerUseCase
class ResetAllStudyRoomsUseCase(
    private val studyRoomService: StudyRoomService
) {

    fun execute() {
        studyRoomService.deleteAllSeatApplications()
    }
}
