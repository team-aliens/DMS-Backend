package team.aliens.dms.domain.point.exception

import team.aliens.dms.common.error.DmsException

object InvalidPointFilterRangeException : DmsException(
    PointErrorCode.INVALID_POINT_FILTER_RANGE
)

object PointHistoryCanNotCancelException : DmsException(
    PointErrorCode.POINT__CAN_NOT_CANCEL
)

object PointHistoryNotFoundException : DmsException(
    PointErrorCode.POINT__NOT_FOUND
)

object PointOptionNameExistsException : DmsException(
    PointErrorCode.POINT_OPTION_NAME_EXISTS
)

object PointOptionNotFoundException : DmsException(
    PointErrorCode.POINT_OPTION_NOT_FOUND
)
