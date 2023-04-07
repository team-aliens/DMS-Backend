package team.aliens.dms.domain.point.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.point.exception.error.PointHistoryErrorCode

object PointHistoryCanNotCancelException : DmsException(
    PointHistoryErrorCode.POINT_HISTORY_CAN_NOT_CANCEL
)
