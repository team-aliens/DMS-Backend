package team.aliens.dms

import team.aliens.dms.contract.model.notification.DeviceTokenInfo

data class SaveDeviceTokenMessage(
    val deviceTokenInfo: DeviceTokenInfo
) : TopicDeviceTokenMessage()
