package team.aliens.dms.domain.point.error

import team.aliens.dms.common.error.ErrorProperty

enum class PointOptionErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    POINT_OPTION_NAME_EXISTS(409, "Point Option Exists"),
    POINT_OPTION_NOT_FOUND(404, "Point Option Not Found"),
    POINT_OPTION_SCHOOL_MISMATCH(401, "Point Option School Mismatch")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}