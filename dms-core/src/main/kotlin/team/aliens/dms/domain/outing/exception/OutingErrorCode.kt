package team.aliens.dms.domain.outing.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class OutingErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    OUTING_AVAILABLE_TIME_MISMATCH(ErrorStatus.FORBIDDEN, "Outing Available Time Mismatch", 1),
    OUTING_TYPE_MISMATCH(ErrorStatus.FORBIDDEN, "Outing Type Mismatch", 2),

    OUTING_APPLICATION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Outing Application Not Found", 1),
    OUTING_TYPE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Outing Type Not Found", 2),

    OUTING_APPLICATION_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Outing Application Already Exists", 1),
    OUTING_TYPE_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Outing Type Already Exists", 2),
    OUTING_AVAILABLE_TIME_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Outing Available Time Already Exists", 1),
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "OUTING-$status-$sequence"
}
