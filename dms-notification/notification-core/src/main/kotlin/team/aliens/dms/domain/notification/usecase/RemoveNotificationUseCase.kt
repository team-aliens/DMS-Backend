package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notification.service.NotificationService
import java.util.UUID

@UseCase
class RemoveNotificationUseCase(
    private val securityService: SecurityService,
    private val notificationService: NotificationService
) {

    fun execute(notificationOfUserId: UUID) {
        val userId = securityService.getCurrentUserId()
        notificationService.deleteNotificationOfUserByUserIdAndId(
            userId = userId,
            notificationOfUserId = notificationOfUserId
        )
    }
}
