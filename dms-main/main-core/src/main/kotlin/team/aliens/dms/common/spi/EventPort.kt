package team.aliens.dms.common.spi

import team.aliens.dms.GroupNotificationInfo
import team.aliens.dms.SingleNotificationInfo



interface NotificationEventPort {

    fun publishNotification(singleNotificationInfo: SingleNotificationInfo)

    fun publishNotificationToApplicant(groupNotificationInfo: GroupNotificationInfo)
}

interface EventPort : NotificationEventPort
