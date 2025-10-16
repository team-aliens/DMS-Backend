package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.exception.error.SeatErrorCode

object SeatCanNotAppliedException : DmsException(
    SeatErrorCode.SEAT_CAN_NOT_APPLY
)
