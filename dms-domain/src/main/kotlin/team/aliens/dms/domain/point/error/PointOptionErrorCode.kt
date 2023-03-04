package team.aliens.dms.domain.point.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class PointOptionErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    POINT_OPTION_NAME_EXISTS(ErrorStatus.CONFLICT, "Point Option Exists"),
    POINT_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Point Option Not Found"),
    POINT_OPTION_SCHOOL_MISMATCH(ErrorStatus.UNAUTHORIZED, "Point Option School Mismatch")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
