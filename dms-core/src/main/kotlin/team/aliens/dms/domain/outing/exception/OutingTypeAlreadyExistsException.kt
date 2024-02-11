package team.aliens.dms.domain.outing.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.outing.exception.error.OutingApplicationErrorCode

object OutingTypeAlreadyExistsException : DmsException(
    OutingApplicationErrorCode.OUTING_TYPE_ALREADY_EXISTS
)