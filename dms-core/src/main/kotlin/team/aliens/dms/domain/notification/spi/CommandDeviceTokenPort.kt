package team.aliens.dms.domain.notification.spi

import java.util.UUID
import team.aliens.dms.domain.notification.model.DeviceToken

interface CommandDeviceTokenPort {

    fun saveDeviceToken(deviceToken: DeviceToken): DeviceToken

    fun deleteDeviceTokenByUserId(userId: UUID)
}