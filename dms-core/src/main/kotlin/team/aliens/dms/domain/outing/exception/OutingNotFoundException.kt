package team.aliens.dms.domain.outing.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.outing.exception.error.OutingErrorCode

object OutingNotFoundException : DmsException(
    OutingErrorCode.OUTING_NOT_FOUND
)
