package team.aliens.dms.common.spi

import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification

interface NotificationEventPort {

    fun publishNotification(deviceToken: DeviceToken, notification: Notification)

    fun publishNotificationToApplicant(deviceTokens: List<DeviceToken>, notification: Notification)
}

interface EventPort : NotificationEventPort
