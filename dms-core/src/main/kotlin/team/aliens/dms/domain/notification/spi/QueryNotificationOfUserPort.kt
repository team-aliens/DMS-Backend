package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.common.dto.PageData
import java.util.UUID

interface QueryNotificationOfUserPort {

    fun queryNotificationOfUserByUserId(userId: UUID, pageData: PageData): List<NotificationOfUser>

    fun queryNotificationOfUserById(notificationOfUserId: UUID): NotificationOfUser?
}
