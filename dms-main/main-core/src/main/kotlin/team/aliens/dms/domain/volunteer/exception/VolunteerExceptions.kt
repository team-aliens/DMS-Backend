package team.aliens.dms.domain.volunteer.exception

import team.aliens.dms.common.error.DmsException

object VolunteerNotAvailableException : DmsException(
    VolunteerErrorCode.VOLUNTEER_NOT_AVAILABLE
)

object VolunteerApplicationNotFoundException : DmsException(
    VolunteerErrorCode.VOLUNTEER_APPLICATION_NOT_FOUND
)

object VolunteerNotFoundException : DmsException(
    VolunteerErrorCode.VOLUNTEER_NOT_FOUND
)

object VolunteerApplicationAlreadyAssigned : DmsException(
    VolunteerErrorCode.VOLUNTEER_APPLICATION_ALREADY_ASSIGNED
)

object VolunteerApplicationNotAssigned : DmsException(
    VolunteerErrorCode.VOLUNTEER_APPLICATION_NOT_ASSIGNED
)

object VolunteerApplicationAlreadyExists : DmsException(
    VolunteerErrorCode.VOLUNTEER_APPLICATION_ALREADY_EXISTS
)

object VolunteerInvalidScoreRangeException : DmsException(
    VolunteerErrorCode.VOLUNTEER_INVALID_SCORE_RANGE
)
