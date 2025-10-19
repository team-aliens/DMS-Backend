package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notification.dto.NotificationsResponse
import team.aliens.dms.domain.notification.service.NotificationService

@UseCase
class QueryAndReadMyNotificationsUseCase(
    private val securityService: SecurityService,
    private val notificationService: NotificationService
) {

    fun execute(): NotificationsResponse {
        val userId = securityService.getCurrentUserId()
        val notificationsOfUser = notificationService.getNotificationOfUsersByUserId(userId)
        notificationService.saveNotificationsOfUser(notificationsOfUser.map { it.read() })
        return NotificationsResponse.of(notificationsOfUser)
    }
}
