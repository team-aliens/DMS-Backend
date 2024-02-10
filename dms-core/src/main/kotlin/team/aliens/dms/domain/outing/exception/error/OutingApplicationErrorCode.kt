package team.aliens.dms.domain.outing.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class OutingApplicationErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    OUTING_AVAILABLE_TIME_MISMATCH(ErrorStatus.CONFLICT, "Outing Application Time Mismatch", 1),
    OUTING_APPLICATION_EXISTS(ErrorStatus.CONFLICT, "Outing Application Exists", 1),
    OUTING_APPLICATION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Outing Not Found", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "OUTING-$status-$sequence"
}
