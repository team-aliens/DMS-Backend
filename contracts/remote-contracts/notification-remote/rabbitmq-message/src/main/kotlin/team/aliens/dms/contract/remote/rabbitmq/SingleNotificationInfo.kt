package team.aliens.dms

import team.aliens.dms.contract.model.notification.NotificationInfo
import java.util.UUID

class SingleNotificationInfo(

    val userId: UUID,

    override val notificationInfo: NotificationInfo

) : TopicNotificationInfo(notificationInfo)
