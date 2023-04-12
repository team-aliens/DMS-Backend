package team.aliens.dms.domain.point.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.point.exception.error.PointHistoryErrorCode

object InvalidPointFilterRangeException : DmsException(
    PointHistoryErrorCode.INVALID_POINT_FILTER_RANGE
)
