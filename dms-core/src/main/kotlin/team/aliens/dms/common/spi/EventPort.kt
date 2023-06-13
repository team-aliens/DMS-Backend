package team.aliens.dms.common.spi

import team.aliens.dms.domain.notification.model.Notification

interface EventPort {
    fun publishNotification(deviceToken: String, notification: Notification)
    fun publishNotificationToAll(notification: Notification)
}
