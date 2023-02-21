package team.aliens.dms.domain.point.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.point.error.PointOptionErrorCode

object PointOptionSchoolMismatchException : DmsException(
    PointOptionErrorCode.POINT_OPTION_SCHOOL_MISMATCH
)