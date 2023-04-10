package team.aliens.dms.domain.remain.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class RemainAvailableTimeErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    REMAIN_CAN_NOT_APPLIED(ErrorStatus.FORBIDDEN, "Remain Can Not Apply", "REMAIN-403-1"),
    REMAIN_AVAILABLE_TIME_CAN_NOT_ACCESS(ErrorStatus.FORBIDDEN, "Remain Available Time Can Not Access", "REMAIN-403-2"),

    REMAIN_AVAILABLE_TIME_NOT_FOUND(ErrorStatus.NOT_FOUND, "Remain Available Time Not Found", "REMAIN-404-1"),
    ;

    override fun status() = status
    override fun message() = message
    override fun code(): String = code
}
