package team.aliens.dms.domain.notification.spi

import java.util.UUID
import team.aliens.dms.domain.notification.model.NotificationOfUser

interface QueryNotificationOfUserPort {

    fun queryNotificationOfUserByUserId(userId: UUID): List<NotificationOfUser>

    fun queryNotificationOfUserById(notificationOfUserId: UUID): NotificationOfUser?
}