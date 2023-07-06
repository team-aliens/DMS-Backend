package team.aliens.dms.domain.notification.dto.request

import team.aliens.dms.domain.notification.model.OperatingSystem

data class SetDeviceTokenWebRequest(
    val deviceToken: String,
    val deviceId: String,
    val operatingSystem: OperatingSystem
)

data class RemoveDeviceTokenWebRequest(
    val deviceId: String,
    val operatingSystem: OperatingSystem
)
