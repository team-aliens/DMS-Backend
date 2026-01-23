package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notification.service.NotificationService
import java.util.UUID

@UseCase
class ReadNotificationUseCase(
    private val notificationService: NotificationService
) {
    fun execute(notificationOfUserId: UUID) {
        val notificationOfUser = notificationService.getNotificationOfUserById(notificationOfUserId)
        notificationService.saveNotificationOfUser(notificationOfUser.read())
    }
}
