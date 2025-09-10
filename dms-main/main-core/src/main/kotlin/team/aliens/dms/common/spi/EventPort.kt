package team.aliens.dms.common.spi

import team.aliens.dms.contract.model.NotificationInfo
import java.util.UUID

interface NotificationEventPort {

    fun publishNotification(userId: UUID, notificationInfo: NotificationInfo)

    fun publishNotificationToApplicant(userIds: List<UUID>, notificationInfo: NotificationInfo)
}

interface EventPort : NotificationEventPort
