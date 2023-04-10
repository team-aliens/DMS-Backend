package team.aliens.dms.domain.point.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class PointHistoryErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    POINT_HISTORY_NOT_FOUND(ErrorStatus.NOT_FOUND, "Point History Not Found", "POINT-404-1"),

    INVALID_POINT_FILTER_RANGE(ErrorStatus.BAD_REQUEST, "Invalid Point Filter Range", "POINT-400-1"),

    POINT_HISTORY_CAN_NOT_CANCEL(ErrorStatus.BAD_REQUEST, "Point History Can Not Cancel", "POINT-400-2")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
