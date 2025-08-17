package team.aliens.dms.domain.point.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class PointErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    POINT__NOT_FOUND(ErrorStatus.NOT_FOUND, "Point History Not Found", 1),
    POINT_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Point Option Not Found", 2),

    INVALID_POINT_FILTER_RANGE(ErrorStatus.BAD_REQUEST, "Invalid Point Filter Range", 1),
    POINT__CAN_NOT_CANCEL(ErrorStatus.BAD_REQUEST, "Point History Can Not Cancel", 2),

    POINT_OPTION_NAME_EXISTS(ErrorStatus.CONFLICT, "Point Option Exists", 1),
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "POINT-$status-$sequence"
}
