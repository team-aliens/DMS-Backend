package team.aliens.dms.event

import org.springframework.context.ApplicationEvent
import team.aliens.dms.domain.notification.model.Notification

open class TopicNotificationEvent(
    val notification: Notification
) : ApplicationEvent(notification)

open class GroupNotificationEvent(
    val tokens: List<String>,
    notification: Notification
) : TopicNotificationEvent(notification)

class SingleNotificationEvent(
    val token: String,
    notification: Notification
) : TopicNotificationEvent(notification)
