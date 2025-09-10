package team.aliens.dms

import team.aliens.dms.contract.model.NotificationInfo
import java.util.UUID

data class GroupNotificationInfo(

    val userIds: List<UUID>,

    val notificationInfo: NotificationInfo

)
