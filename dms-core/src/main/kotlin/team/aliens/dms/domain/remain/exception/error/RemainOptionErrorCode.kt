package team.aliens.dms.domain.remain.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class RemainOptionErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    REMAIN_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Remain Option Not Found", "REMAIN-404-2")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
