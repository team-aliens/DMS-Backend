package team.aliens.dms.domain.remain.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class RemainAvailableTimeErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    REMAIN_CAN_NOT_APPLIED(ErrorStatus.FORBIDDEN, "Remain Can Not Apply"),
    REMAIN_AVAILABLE_TIME_CAN_NOT_ACCESS(ErrorStatus.FORBIDDEN, "Remain Available Time Can Not Access"),

    REMAIN_AVAILABLE_TIME_NOT_FOUND(ErrorStatus.NOT_FOUND, "Remain Available Time Not Found"),
    ;

    override fun status() = status
    override fun message() = message
}
