package team.aliens.dms.domain.remain.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class RemainStatusErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    REMAIN_STATUS_NOT_FOUND(ErrorStatus.NOT_FOUND, "Remain Status Not Found", "REMAIN-404-3")
    ;

    override fun status() = status
    override fun message() = message
    override fun code(): String = code
}
