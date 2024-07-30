package team.aliens.dms.domain.notification.spi

import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.notification.model.NotificationOfUser
import java.util.UUID

interface QueryNotificationOfUserPort {

    fun queryNotificationOfUserByUserId(userId: UUID): List<NotificationOfUser>

    fun queryNotificationOfUserById(notificationOfUserId: UUID): NotificationOfUser?
}
