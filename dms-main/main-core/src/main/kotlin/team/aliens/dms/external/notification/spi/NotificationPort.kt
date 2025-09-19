package team.aliens.dms.external.notification.spi

import team.aliens.dms.contract.model.DeviceTokenInfo
import java.util.UUID

interface NotificationPort {

    fun saveDeviceToken(deviceToken: DeviceTokenInfo): DeviceTokenInfo

    fun deleteDeviceTokenByUserId(userId: UUID)

    fun checkDeviceTokenByUserId(userId: UUID): Boolean
}
