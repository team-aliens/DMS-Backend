package team.aliens.dms.event

import org.springframework.context.ApplicationEvent
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification

open class TopicNotificationEvent(
    val notification: Notification
) : ApplicationEvent(notification)

open class GroupNotificationEvent(
    val deviceTokens: List<DeviceToken>,
    notification: Notification
) : TopicNotificationEvent(notification)

class SingleNotificationEvent(
    val deviceToken: DeviceToken,
    notification: Notification
) : TopicNotificationEvent(notification)
