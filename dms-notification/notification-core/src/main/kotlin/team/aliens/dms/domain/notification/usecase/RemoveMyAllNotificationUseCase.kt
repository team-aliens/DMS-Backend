package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notification.service.NotificationService

@UseCase
class RemoveMyAllNotificationUseCase(
    private val securityService: SecurityService,
    private val notificationService: NotificationService
) {

    fun execute() {
        val userId = securityService.getCurrentUserId()
        notificationService.deleteNotificationOfUserByUserId(userId)
    }
}
