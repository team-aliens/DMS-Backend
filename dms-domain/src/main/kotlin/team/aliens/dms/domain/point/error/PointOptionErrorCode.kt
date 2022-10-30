package team.aliens.dms.domain.point.error

import team.aliens.dms.common.error.ErrorProperty

enum class PointOptionErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    POINT_OPTION_NOT_FOUND(404, "Point Option Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}