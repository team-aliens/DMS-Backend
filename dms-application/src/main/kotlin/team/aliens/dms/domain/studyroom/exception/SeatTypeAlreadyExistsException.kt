package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.SeatTypeErrorCode

object SeatTypeAlreadyExistsException : DmsException(
    SeatTypeErrorCode.SEAT_TYPE_ALREADY_EXISTS
)
