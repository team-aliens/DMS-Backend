package team.aliens.dms

import team.aliens.dms.contract.model.NotificationInfo
import java.util.UUID

data class SingleNotificationInfo(

    val userId: UUID,

    val notificationInfo: NotificationInfo

)
