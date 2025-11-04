package team.aliens.dms.common.spi

import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import team.aliens.dms.contract.model.notification.NotificationInfo
import java.util.UUID

interface NotificationEventPort {

    fun publishNotification(userId: UUID, notificationInfo: NotificationInfo)

    fun publishNotificationToApplicant(userIds: List<UUID>, notificationInfo: NotificationInfo)
}

interface DeviceTokenEventPort {

    fun publishSaveDeviceToken(deviceTokenInfo: DeviceTokenInfo)

    fun publishDeleteDeviceToken(userId: UUID)
}

interface EventPort : NotificationEventPort, DeviceTokenEventPort
