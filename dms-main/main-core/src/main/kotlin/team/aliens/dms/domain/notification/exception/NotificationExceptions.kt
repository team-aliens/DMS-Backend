package team.aliens.dms.domain.notification.exception

import team.aliens.dms.common.error.DmsException

object DeviceTokenNotFoundException : DmsException(
    NotificationErrorCode.DEVICE_TOKEN_NOT_FOUND
)

object NotificationOfUserNotFoundException : DmsException(
    NotificationErrorCode.NOTIFICATION_OF_USER_NOT_FOUND
)

object NotificationSendFailedException : DmsException(
    NotificationErrorCode.NOTIFICATION_SEND_FAILED
)
