package team.aliens.dms.common.spi

import team.aliens.dms.domain.notification.model.Notification

interface NotificationEventPort {
    fun publishNotification(deviceToken: String, notification: Notification)
    fun publishNotificationToAll(notification: Notification)
}

interface EventPort : NotificationEventPort
