package team.aliens.dms.domain.point.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.point.error.PointOptionErrorCode

object PointOptionNameExistsException : DmsException(
    PointOptionErrorCode.POINT_OPTION_NAME_EXISTS
)
