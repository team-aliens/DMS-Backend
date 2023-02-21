package team.aliens.dms.domain.remain.error

import team.aliens.dms.common.error.ErrorProperty

enum class RemainOptionErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    REMAIN_OPTION_NOT_FOUND(404, "Remain Option Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}