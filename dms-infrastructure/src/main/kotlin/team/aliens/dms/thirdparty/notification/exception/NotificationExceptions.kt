package team.aliens.dms.thirdparty.notification.exception

import team.aliens.dms.common.error.DmsException

object NotificationException : DmsException(
    NotificationErrorCode.NOTIFICATION_PROCESS_FAILED
)
