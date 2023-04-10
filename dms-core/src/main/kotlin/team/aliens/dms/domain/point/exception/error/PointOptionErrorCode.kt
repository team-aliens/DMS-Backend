package team.aliens.dms.domain.point.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class PointOptionErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    POINT_OPTION_NAME_EXISTS(ErrorStatus.CONFLICT, "Point Option Exists", "POINT-409-1"),

    POINT_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Point Option Not Found", "POINT-404-2"),

    POINT_OPTION_SCHOOL_MISMATCH(ErrorStatus.UNAUTHORIZED, "Point Option School Mismatch", "POINT-401-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
