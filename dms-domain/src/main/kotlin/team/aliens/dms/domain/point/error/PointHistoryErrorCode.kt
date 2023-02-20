package team.aliens.dms.domain.point.error

import team.aliens.dms.common.error.ErrorProperty

enum class PointHistoryErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    POINT_HISTORY_NOT_FOUND(404, "Point History Not Found"),
    INVALID_FILTER_REQUEST(400, "Required minPoint and maxPoint")
    POINT_HISTORY_CAN_NOT_CANCEL(400, "Point History Can Not Cancel")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}