package team.aliens.dms.domain.remain.exception

import team.aliens.dms.common.error.DmsException

object RemainAvailableTimeCanNotAccessException : DmsException(
    RemainErrorCode.REMAIN__CAN_NOT_ACCESS
)

object RemainAvailableTimeNotFoundException : DmsException(
    RemainErrorCode.REMAIN__NOT_FOUND
)

object RemainCanNotAppliedException : DmsException(
    RemainErrorCode.REMAIN_CAN_NOT_APPLIED
)

object RemainOptionNotFoundException : DmsException(
    RemainErrorCode.REMAIN_OPTION_NOT_FOUND
)

object RemainStatusNotFound : DmsException(
    RemainErrorCode.REMAIN_STATUS_NOT_FOUND
)
