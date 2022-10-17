package team.aliens.dms.domain.point.exception

import team.aliens.dms.domain.point.error.PointHistoryErrorCode
import team.aliens.dms.global.error.DmsException

object PointHistoryNotFoundException : DmsException(
    PointHistoryErrorCode.POINT_HISTORY_NOT_FOUND
)