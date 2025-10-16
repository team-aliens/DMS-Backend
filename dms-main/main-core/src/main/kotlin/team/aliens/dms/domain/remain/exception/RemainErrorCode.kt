package team.aliens.dms.domain.remain.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class RemainErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    REMAIN_CAN_NOT_APPLIED(ErrorStatus.FORBIDDEN, "Remain Can Not Apply", 1),
    REMAIN__CAN_NOT_ACCESS(ErrorStatus.FORBIDDEN, "Remain Available Time Can Not Access", 2),

    REMAIN__NOT_FOUND(ErrorStatus.NOT_FOUND, "Remain Available Time Not Found", 1),
    REMAIN_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Remain Option Not Found", 2),

    REMAIN_STATUS_NOT_FOUND(ErrorStatus.NOT_FOUND, "Remain Status Not Found", 3)
    ;

    override fun status() = status
    override fun message() = message
    override fun code(): String = "REMAIN-$status-$sequence"
}
