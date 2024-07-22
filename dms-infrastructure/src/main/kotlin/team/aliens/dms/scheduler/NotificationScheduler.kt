package team.aliens.dms.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.usecase.DeleteOldNotificationsUseCase

@Component
class NotificationScheduler(
    private val deleteOldNotificationsUseCase: DeleteOldNotificationsUseCase
) {

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    fun deleteOldNotifications() {
        deleteOldNotificationsUseCase.execute()
    }
}
