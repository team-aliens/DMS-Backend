package team.aliens.dms.domain.remain.error

import team.aliens.dms.common.error.ErrorProperty

enum class RemainAvailableTimeErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    REMAIN_CAN_NOT_APPLIED(403, "Remain Can Not Apply"),
    REMAIN_AVAILABLE_TIME_CAN_NOT_ACCESS(403, "Remain Available Time Can Not Access"),

    REMAIN_AVAILABLE_TIME_NOT_FOUND(404, "Remain Available Time Not Found"),
    ;

    override fun status() = status
    override fun message() = message
}