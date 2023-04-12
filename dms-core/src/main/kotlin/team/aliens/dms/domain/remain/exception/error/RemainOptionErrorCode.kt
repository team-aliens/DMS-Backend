package team.aliens.dms.domain.remain.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class RemainOptionErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    REMAIN_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Remain Option Not Found", 2)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "REMAIN-$status-$sequence"
}
