package team.aliens.dms.event

import org.springframework.context.ApplicationEvent
import team.aliens.dms.contract.model.NotificationInfo
import java.util.UUID

open class TopicNotificationEvent(
    val notificationInfo: NotificationInfo
) : ApplicationEvent(notificationInfo)

open class GroupNotificationEvent(
    val userIds: List<UUID>,
    notificationInfo: NotificationInfo
) : TopicNotificationEvent(notificationInfo)

class SingleNotificationEvent(
    val userId: UUID,
    notificationInfo: NotificationInfo
) : TopicNotificationEvent(notificationInfo)
