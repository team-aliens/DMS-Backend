package team.aliens.dms.global.security.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SecurityErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    INVALID_TOKEN(ErrorStatus.UNAUTHORIZED, "Invalid Token", 1),
    EXPIRED_TOKEN(ErrorStatus.UNAUTHORIZED, "Expired Token", 2)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "SECURITY-$status-$sequence"
}
