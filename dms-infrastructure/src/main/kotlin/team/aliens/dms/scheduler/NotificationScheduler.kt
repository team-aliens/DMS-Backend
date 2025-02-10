package team.aliens.dms.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.usecase.DeleteOldNotificationsUseCase

@Component
class NotificationScheduler(
    private val deleteOldNotificationsUseCase: DeleteOldNotificationsUseCase
) {

    /**
     * 매일 밤 12시 마다 60일이 지난 알림들 삭제
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    fun deleteOldNotifications() {
        deleteOldNotificationsUseCase.execute()
    }
}
