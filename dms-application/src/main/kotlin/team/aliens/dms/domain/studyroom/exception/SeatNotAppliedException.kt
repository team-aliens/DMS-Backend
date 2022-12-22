package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.SeatErrorCode

object SeatNotAppliedException : DmsException(
    SeatErrorCode.SEAT_NOT_APPLIED
)