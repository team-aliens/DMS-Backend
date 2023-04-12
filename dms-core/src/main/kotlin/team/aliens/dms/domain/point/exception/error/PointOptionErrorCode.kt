package team.aliens.dms.domain.point.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class PointOptionErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    POINT_OPTION_NAME_EXISTS(ErrorStatus.CONFLICT, "Point Option Exists", 1),

    POINT_OPTION_SCHOOL_MISMATCH(ErrorStatus.UNAUTHORIZED, "Point Option School Mismatch", 1),

    POINT_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Point Option Not Found", 2),
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "POINT-$status-$sequence"
}
