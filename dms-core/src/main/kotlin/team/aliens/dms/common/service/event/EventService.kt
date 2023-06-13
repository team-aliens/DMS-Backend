package team.aliens.dms.common.service.event

import team.aliens.dms.domain.notification.model.Notification

interface EventService {
    fun publishNotification(deviceToken: String, notification: Notification)
    fun publishNotificationToAll(notification: Notification)
}
