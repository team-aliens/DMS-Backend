package team.aliens.dms.domain.remain.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.remain.exception.error.RemainAvailableTimeErrorCode

object RemainAvailableTimeCanNotAccessException : DmsException(
    RemainAvailableTimeErrorCode.REMAIN_AVAILABLE_TIME_CAN_NOT_ACCESS
)
