package team.aliens.dms.domain.outing.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.outing.exception.error.OutingErrorCode

object OutingApplicationAlreadyExistsException : DmsException(
    OutingErrorCode.OUTING_APPLICATION_EXISTS
)
