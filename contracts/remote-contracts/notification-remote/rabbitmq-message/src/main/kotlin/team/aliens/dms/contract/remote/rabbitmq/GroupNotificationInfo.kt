package team.aliens.dms

import team.aliens.dms.contract.model.notification.NotificationInfo
import java.util.UUID

class GroupNotificationInfo(

    val userIds: List<UUID>,

    override val notificationInfo: NotificationInfo

) : TopicNotificationInfo(notificationInfo)
