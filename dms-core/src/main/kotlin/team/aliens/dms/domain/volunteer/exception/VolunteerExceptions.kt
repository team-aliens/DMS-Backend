package team.aliens.dms.domain.volunteer.exception

import team.aliens.dms.common.error.DmsException

object VolunteerApplicationNotFoundException : DmsException(
    VolunteerErrorCode.VOLUNTEER_APPLICATION_NOT_FOUND
)

object VolunteerApplicationAlreadyAssigned : DmsException(
    VolunteerErrorCode.VOLUNTEER_APPLICATION_ALREADY_ASSIGNED
)

object VolunteerNotFoundException : DmsException(
    VolunteerErrorCode.VOLUNTEER_NOT_FOUND
)
