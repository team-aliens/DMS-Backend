package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.SeatTypeErrorCode

object SeatTypeInUseException : DmsException(
    SeatTypeErrorCode.SEAT_TYPE_IN_USE
)
