package team.aliens.dms.domain.notification.dto

import team.aliens.dms.domain.notification.model.OperatingSystem

class RemoveDeviceTokenRequest(
    val deviceId: String,
    val operatingSystem: OperatingSystem
)
