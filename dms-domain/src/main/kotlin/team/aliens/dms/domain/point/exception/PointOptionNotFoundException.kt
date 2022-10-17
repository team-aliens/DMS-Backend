package team.aliens.dms.domain.point.exception

import team.aliens.dms.domain.point.error.PointOptionErrorCode
import team.aliens.dms.global.error.DmsException

object PointOptionNotFoundException : DmsException(
    PointOptionErrorCode.POINT_OPTION_NOT_FOUND
)