package team.aliens.dms.domain.outing.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.outing.exception.error.OutingErrorCode

object OutingAvailableTimeMismatchException :DmsException(
    OutingErrorCode.OUTING_AVAILABLE_TIME_MISMATCH
)
