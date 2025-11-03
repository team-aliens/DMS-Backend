package team.aliens.dms

import java.util.UUID

data class DeleteDeviceTokenMessage(
    val userId: UUID
) : TopicDeviceTokenMessage()
