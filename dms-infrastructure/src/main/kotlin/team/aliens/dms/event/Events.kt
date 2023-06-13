package team.aliens.dms.event

import org.springframework.context.ApplicationEvent
import team.aliens.dms.domain.notification.model.Notification

open class GroupNotificationEvent(
    val notification: Notification
) : ApplicationEvent(notification)

class SingleNotificationEvent(
    val deviceToken: String,
    notification: Notification
) : GroupNotificationEvent(notification)
