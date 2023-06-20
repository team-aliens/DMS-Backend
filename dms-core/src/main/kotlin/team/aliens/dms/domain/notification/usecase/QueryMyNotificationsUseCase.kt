package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notification.dto.NotificationsResponse
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryMyNotificationsUseCase(
    private val userService: UserService,
    private val notificationService: NotificationService
) {

    fun execute(): NotificationsResponse {
        val user = userService.getCurrentUser()
        return NotificationsResponse.of(
            notificationService.getNotificationOfUsersByUserId(user.id)
        )
    }
}
