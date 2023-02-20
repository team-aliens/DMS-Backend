package team.aliens.dms.domain.point.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.point.error.PointHistoryErrorCode

object InvalidFilterRequestException : DmsException(
    PointHistoryErrorCode.INVALID_FILTER_REQUEST
)