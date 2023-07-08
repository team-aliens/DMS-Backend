package team.aliens.dms.domain.notification.spi

import java.util.UUID
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.notification.model.NotificationOfUser

interface QueryNotificationOfUserPort {

    fun queryNotificationOfUserByUserId(userId: UUID, pageData: PageData): List<NotificationOfUser>

    fun queryNotificationOfUserById(notificationOfUserId: UUID): NotificationOfUser?
}
