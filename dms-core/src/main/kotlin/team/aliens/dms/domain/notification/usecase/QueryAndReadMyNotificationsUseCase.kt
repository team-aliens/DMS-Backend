package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notification.dto.NotificationsResponse
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class QueryAndReadMyNotificationsUseCase(
    private val userService: UserService,
    private val notificationService: NotificationService
) {

    fun execute(): NotificationsResponse {
        val user = userService.getCurrentUser()
        val notificationsOfUser = notificationService.getNotificationOfUsersByUserId(user.id)
        notificationService.saveNotificationsOfUser(notificationsOfUser.map { it.read() })
        return NotificationsResponse.of(notificationsOfUser)
    }
}
