package team.aliens.dms.domain.outing.exception

import team.aliens.dms.common.error.DmsException

object OutingApplicationAlreadyExistsException : DmsException(
    OutingErrorCode.OUTING_APPLICATION_ALREADY_EXISTS
)

object OutingApplicationNotFoundException : DmsException(
    OutingErrorCode.OUTING_APPLICATION_NOT_FOUND
)

object OutingAvailableTimeMismatchException : DmsException(
    OutingErrorCode.OUTING_AVAILABLE_TIME_MISMATCH
)
