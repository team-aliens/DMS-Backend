package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveNotificationUseCase(
    private val userService: UserService,
    private val notificationService: NotificationService
) {

    fun execute(notificationOfUserId: UUID) {
        val user = userService.getCurrentUser()
        notificationService.deleteNotificationOfUserByUserIdAndId(
            userId = user.id,
            notificationOfUserId = notificationOfUserId
        )
    }
}
