package team.aliens.dms.domain.outing.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.outing.exception.error.OutingApplicationErrorCode

object OutingApplicationTimeMismatchException :DmsException(
    OutingApplicationErrorCode.OUTING_AVAILABLE_TIME_MISMATCH
)
