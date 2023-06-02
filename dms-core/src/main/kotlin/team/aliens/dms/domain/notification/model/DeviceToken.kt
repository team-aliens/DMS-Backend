package team.aliens.dms.domain.notification.model

import java.util.UUID

data class DeviceToken(
    val userId: UUID,
    val schoolId: UUID,
    val deviceToken: String
)
