package team.aliens.dms.domain.remain.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.remain.exception.error.RemainAvailableTimeErrorCode

object RemainCanNotAppliedException : DmsException(
    RemainAvailableTimeErrorCode.REMAIN_CAN_NOT_APPLIED
)
