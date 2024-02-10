package team.aliens.dms.domain.outing.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.outing.exception.error.OutingApplicationErrorCode

object OutingApplicationNotFoundException : DmsException(
    OutingApplicationErrorCode.OUTING_APPLICATION_NOT_FOUND
)
