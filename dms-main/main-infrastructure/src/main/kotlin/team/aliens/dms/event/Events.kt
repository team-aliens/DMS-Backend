package team.aliens.dms.event

import org.springframework.context.ApplicationEvent
import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import team.aliens.dms.contract.model.notification.NotificationInfo
import java.util.UUID

sealed class NotificationEvent(
    source: Any
) : ApplicationEvent(source)

class GroupNotificationEvent(
    val userIds: List<UUID>,
    val notificationInfo: NotificationInfo
) : NotificationEvent(notificationInfo)

class SingleNotificationEvent(
    val userId: UUID,
    val notificationInfo: NotificationInfo
) : NotificationEvent(notificationInfo)

class TopicNotificationEvent(
    val notificationInfo: NotificationInfo
) : NotificationEvent(notificationInfo)

sealed class DeviceTokenEvent(
    source: Any
) : ApplicationEvent(source)

class SaveDeviceTokenEvent(
    val deviceTokenInfo: DeviceTokenInfo
) : DeviceTokenEvent(deviceTokenInfo)

class DeleteDeviceTokenEvent(
    val userId: UUID
) : DeviceTokenEvent(userId)
