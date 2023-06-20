package team.aliens.dms.domain.notification.spi

import java.util.UUID
import team.aliens.dms.domain.notification.model.NotificationOfUser

interface NotificationOfUserPort {

    fun saveNotificationOfUser(notificationOfUser: NotificationOfUser): NotificationOfUser

    fun saveNotificationsOfUser(notificationsOfUser: List<NotificationOfUser>)

    fun queryNotificationOfUserByUserId(userId: UUID): List<NotificationOfUser>
}
