package team.aliens.dms.domain.notification.spi

import java.util.UUID
import team.aliens.dms.domain.notification.model.NotificationOfUser

interface CommandNotificationOfUserPort {

    fun saveNotificationOfUser(notificationOfUser: NotificationOfUser): NotificationOfUser

    fun saveNotificationsOfUser(notificationsOfUser: List<NotificationOfUser>)

    fun deleteNotificationOfUserById(notificationOfUserId: UUID)

    fun deleteNotificationOfUserByUserId(userId: UUID)
}