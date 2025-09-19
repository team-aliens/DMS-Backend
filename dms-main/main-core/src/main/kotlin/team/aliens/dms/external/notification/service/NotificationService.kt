package team.aliens.dms.external.notification.service

import team.aliens.dms.contract.model.DeviceTokenInfo
import java.util.UUID

interface NotificationService {

    fun checkDeviceTokenByUserId(userId: UUID): Boolean

    fun deleteDeviceTokenByUserId(userId: UUID)

    fun saveDeviceToken(deviceToken: DeviceTokenInfo): DeviceTokenInfo
}
