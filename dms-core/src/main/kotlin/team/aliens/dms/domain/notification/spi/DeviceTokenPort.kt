package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.DeviceToken
import java.util.UUID

interface DeviceTokenPort {

    fun saveDeviceToken(deviceToken: DeviceToken): DeviceToken

    fun queryDeviceTokenByUserId(userId: UUID): DeviceToken?

    fun queryDeviceTokenByToken(token: String): DeviceToken?

    fun deleteDeviceTokenByUserId(userId: UUID)
}
