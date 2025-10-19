package team.aliens.dms.domain.notification.model

import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import java.util.UUID

data class DeviceToken(
    val id: UUID = UUID(0, 0),
    val userId: UUID,
    val schoolId: UUID,
    val token: String
) {
    companion object {
        fun from(deviceTokenInfo: DeviceTokenInfo): DeviceToken {
            return DeviceToken(
                id = deviceTokenInfo.id,
                userId = deviceTokenInfo.userId,
                schoolId = deviceTokenInfo.schoolId,
                token = deviceTokenInfo.token
            )
        }
    }

    fun toDeviceTokenInfo(): DeviceTokenInfo {
        return DeviceTokenInfo(
            id = id,
            userId = userId,
            schoolId = schoolId,
            token = token
        )
    }
}
