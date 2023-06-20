package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.NotificationOfUser
import java.util.UUID

interface NotificationOfUserPort {

    fun saveNotificationOfUser(notificationOfUser: NotificationOfUser): NotificationOfUser

    fun saveNotificationsOfUser(notificationsOfUser: List<NotificationOfUser>)

    fun queryNotificationOfUserByUserId(userId: UUID): List<NotificationOfUser>

    fun queryNotificationOfUserById(notificationOfUserId: UUID): NotificationOfUser?

    fun deleteNotificationOfUserById(notificationOfUserId: UUID)

    fun deleteNotificationOfUserByUserId(userId: UUID)
}
