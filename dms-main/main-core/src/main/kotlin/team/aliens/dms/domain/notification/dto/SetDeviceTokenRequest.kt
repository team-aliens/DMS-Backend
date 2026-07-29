package team.aliens.dms.domain.notification.dto

import team.aliens.dms.domain.notification.model.DeviceToken
import java.util.UUID

class SetDeviceTokenRequest(
    val token: String
) {
    fun toDeviceToken(userId: UUID, schoolId: UUID) = DeviceToken(
        userId = userId,
        schoolId = schoolId,
        token = token
    )
}
