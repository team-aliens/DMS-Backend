package team.aliens.dms.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.usecase.ResetAllStudyRoomsUseCase

@Component
class StudyRoomScheduler(
    private val resetAllStudyRoomsUseCase: ResetAllStudyRoomsUseCase
) {

    /**
     * 매일 5시에 자습실 초기화
     */
    @Scheduled(cron = "0 0 5 * * *", zone = "Asia/Seoul")
    fun resetAllStudyRooms() = resetAllStudyRoomsUseCase.execute()
}
