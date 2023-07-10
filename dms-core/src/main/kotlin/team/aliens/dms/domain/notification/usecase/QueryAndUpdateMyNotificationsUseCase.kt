package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.notification.dto.NotificationsResponse
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class QueryAndUpdateMyNotificationsUseCase(
    private val userService: UserService, private val notificationService: NotificationService
) {

    fun execute(pageData: PageData): NotificationsResponse {
        val user = userService.getCurrentUser()
        val notificationsOfUser = notificationService.getNotificationOfUsersByUserId(user.id, pageData)
        notificationService.saveNotificationsOfUser(notificationsOfUser.map { it.copy(isRead = true) })
        return NotificationsResponse.of(notificationsOfUser)
    }
}
