package team.aliens.dms.domain.notification.exception

import team.aliens.dms.common.error.DmsException

object DeviceTokenNotFoundException : DmsException(
    NotificationErrorCode.DEVICE_TOKEN_NOT_FOUND
)
