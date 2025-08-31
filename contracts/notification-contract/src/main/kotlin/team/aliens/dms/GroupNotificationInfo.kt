package team.aliens.dms

import java.util.UUID

data class GroupNotificationInfo(

    val userIds: List<UUID>,

    val notificationInfo: NotificationInfo

)
