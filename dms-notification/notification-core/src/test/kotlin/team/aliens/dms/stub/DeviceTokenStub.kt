package team.aliens.dms.stub

import team.aliens.dms.domain.notification.model.DeviceToken
import java.util.UUID

internal fun createDeviceTokenStub(
    id: UUID = UUID.randomUUID(),
    userId: UUID = UUID.randomUUID(),
    schoolId: UUID = UUID.randomUUID(),
    token: String = "test-device-token"
) = DeviceToken(
    id = id,
    userId = userId,
    schoolId = schoolId,
    token = token
)
