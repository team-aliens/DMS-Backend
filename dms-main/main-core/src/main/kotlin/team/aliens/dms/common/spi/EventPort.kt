package team.aliens.dms.common.spi

import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import team.aliens.dms.contract.model.notification.NotificationInfo
import java.util.UUID

interface NotificationEventPort {

    // 한 명에게 보낼 때도 listOf(userId) 로 넘긴다
    fun publishNotification(userIds: List<UUID>, notificationInfo: NotificationInfo)
}

interface DeviceTokenEventPort {

    fun publishSaveDeviceToken(deviceTokenInfo: DeviceTokenInfo)

    fun publishDeleteDeviceToken(userId: UUID)
}

interface EventPort : NotificationEventPort, DeviceTokenEventPort
