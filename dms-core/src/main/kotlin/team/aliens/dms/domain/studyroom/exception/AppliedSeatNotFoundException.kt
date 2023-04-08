package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.exception.error.SeatErrorCode

object AppliedSeatNotFoundException : DmsException(
    SeatErrorCode.APPLIED_SEAT_NOT_FOUND
)
