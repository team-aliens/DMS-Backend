package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.SeatErrorCode

object SeatAlreadyAppliedException : DmsException(
    SeatErrorCode.SEAT_ALREADY_APPLIED
)
