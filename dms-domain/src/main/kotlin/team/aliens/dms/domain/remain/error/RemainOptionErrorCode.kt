package team.aliens.dms.domain.remain.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class RemainOptionErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    REMAIN_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Remain Option Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
