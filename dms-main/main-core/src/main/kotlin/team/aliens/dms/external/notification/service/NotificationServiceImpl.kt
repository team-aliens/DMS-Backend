package team.aliens.dms.external.notification.service

import team.aliens.dms.contract.model.DeviceTokenInfo
import team.aliens.dms.external.notification.spi.NotificationPort
import java.util.UUID

class NotificationServiceImpl(
    private val notificationPort: NotificationPort
) : NotificationService {
    override fun checkDeviceTokenByUserId(userId: UUID): Boolean {
        return notificationPort.checkDeviceTokenByUserId(userId)
    }

    override fun deleteDeviceTokenByUserId(userId: UUID) {
        return notificationPort.deleteDeviceTokenByUserId(userId)
    }

    override fun saveDeviceToken(deviceToken: DeviceTokenInfo): DeviceTokenInfo {
        return notificationPort.saveDeviceToken(deviceToken)
    }
}
