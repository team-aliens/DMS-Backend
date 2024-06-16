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

object OutingTypeAlreadyExistsException : DmsException(
    OutingErrorCode.OUTING_TYPE_ALREADY_EXISTS
)

object OutingTypeNotFoundException : DmsException(
    OutingErrorCode.OUTING_TYPE_NOT_FOUND
)

object OutingTypeMismatchException : DmsException(
    OutingErrorCode.OUTING_TYPE_MISMATCH
)

object OutingAvailableTimeAlreadyExistsException : DmsException(
    OutingErrorCode.OUTING_AVAILABLE_TIME_ALREADY_EXISTS
)

object OutingAvailableTimeNotFoundException : DmsException(
    OutingErrorCode.OUTING_AVAILABLE_TIME_NOT_FOUND
)
