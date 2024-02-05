package team.aliens.dms.domain.outing.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class OutingErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    OUTING_NOT_FOUND(ErrorStatus.NOT_FOUND, "Outing Not Found", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "OUTING-$status-$sequence"
}
