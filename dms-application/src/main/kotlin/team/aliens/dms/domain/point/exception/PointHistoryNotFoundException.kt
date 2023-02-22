package team.aliens.dms.domain.point.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.point.error.PointHistoryErrorCode

object PointHistoryNotFoundException : DmsException(
    PointHistoryErrorCode.POINT_HISTORY_NOT_FOUND
)
