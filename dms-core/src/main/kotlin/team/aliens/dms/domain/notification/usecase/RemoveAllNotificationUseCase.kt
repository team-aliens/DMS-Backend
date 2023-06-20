package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class RemoveAllNotificationUseCase(
    private val userService: UserService,
    private val notificationService: NotificationService
) {

    fun execute() {
        val user = userService.getCurrentUser()
        notificationService.deleteNotificationOfUserByUserId(user.id)
    }
}
