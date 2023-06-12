package team.aliens.dms.domain.notification.model

import java.util.UUID

data class DeviceToken(
    val id: UUID = UUID(0, 0),
    val userId: UUID,
    val schoolId: UUID,
    val deviceToken: String
)
