package team.aliens.dms.domain.notification.spi

import java.util.UUID
import team.aliens.dms.domain.notification.model.UserNotification

interface UserNotificationPort {

    fun saveUserNotification(userNotification: UserNotification): UserNotification

    fun saveUserNotifications(userNotifications: List<UserNotification>)

    fun queryUserNotificationByUserId(userId: UUID): List<UserNotification>
}
