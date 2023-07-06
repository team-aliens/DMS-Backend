package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.DeviceToken
import java.util.UUID

interface CommandDeviceTokenPort {

    fun saveDeviceToken(deviceToken: DeviceToken): DeviceToken

    fun deleteDeviceTokenByUserId(userId: UUID)

    fun deleteDeviceTokenById(deviceTokenId: UUID)
}
