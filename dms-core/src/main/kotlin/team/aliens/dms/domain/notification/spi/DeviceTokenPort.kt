package team.aliens.dms.domain.notification.spi

import java.util.UUID
import team.aliens.dms.domain.notification.model.DeviceToken

interface DeviceTokenPort {

    fun saveDeviceToken(deviceToken: DeviceToken): DeviceToken

    fun deleteDeviceTokenById(userId: UUID)

    fun queryDeviceTokenById(userId: UUID): DeviceToken?

    fun queryDeviceToenByDeviceToken(deviceToken: String): DeviceToken?
}
