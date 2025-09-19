package team.aliens.dms.contract.model

import java.util.UUID

data class DeviceTokenInfo(
    val id: UUID = UUID(0, 0),
    val userId: UUID,
    val schoolId: UUID,
    val token: String
)
