package team.aliens.dms.domain.remain.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.remain.error.RemainAvailableTimeErrorCode

object RemainAvailableTimeNotFound : DmsException(
    RemainAvailableTimeErrorCode.REMAIN_AVAILABLE_TIME_NOT_FOUND
)