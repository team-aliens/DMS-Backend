package team.aliens.dms.domain.remain.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.remain.exception.error.RemainOptionErrorCode

object RemainOptionNotFoundException : DmsException(
    RemainOptionErrorCode.REMAIN_OPTION_NOT_FOUND
)
