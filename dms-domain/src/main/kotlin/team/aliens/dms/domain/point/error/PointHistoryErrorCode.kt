package team.aliens.dms.domain.point.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class PointHistoryErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    POINT_HISTORY_NOT_FOUND(ErrorStatus.NOT_FOUND, "Point History Not Found"),
    INVALID_POINT_FILTER_RANGE(ErrorStatus.BAD_REQUEST, "Invalid Point Filter Range"),
    POINT_HISTORY_CAN_NOT_CANCEL(ErrorStatus.BAD_REQUEST, "Point History Can Not Cancel")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
