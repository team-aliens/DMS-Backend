package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.notification.service.NotificationService

@SchedulerUseCase
class DeleteOldNotificationsUseCase(
    private val notificationService: NotificationService
) {

    fun execute() {
        notificationService.deleteOldNotifications()
    }
}
