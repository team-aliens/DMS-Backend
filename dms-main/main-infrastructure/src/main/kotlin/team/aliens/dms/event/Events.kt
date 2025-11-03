package team.aliens.dms.event

import org.springframework.context.ApplicationEvent
import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import team.aliens.dms.contract.model.notification.NotificationInfo
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

class SaveDeviceTokenEvent(
    val deviceTokenInfo: DeviceTokenInfo
) : ApplicationEvent(deviceTokenInfo)

class DeleteDeviceTokenEvent(
    val userId: UUID
) : ApplicationEvent(userId)
