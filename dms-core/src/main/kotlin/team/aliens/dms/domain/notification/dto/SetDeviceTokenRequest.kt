package team.aliens.dms.domain.notification.dto

import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.OperatingSystem
import team.aliens.dms.domain.user.model.User

class SetDeviceTokenRequest(
    val deviceId: String,
    val operatingSystem: OperatingSystem,
    val token: String
) {
    fun toDeviceToken(user: User) = DeviceToken(
        userId = user.id,
        schoolId = user.schoolId,
        token = token,
        operatingSystem = operatingSystem,
        deviceId = deviceId
    )
}
