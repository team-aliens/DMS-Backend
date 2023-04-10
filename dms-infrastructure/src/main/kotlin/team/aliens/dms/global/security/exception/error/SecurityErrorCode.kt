package team.aliens.dms.global.security.exception.error

import team.aliens.dms.common.error.ErrorProperty

enum class SecurityErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    INVALID_TOKEN(401, "Invalid Token", "SECURITY-401-1"),
    EXPIRED_TOKEN(401, "Expired Token", "SECURITY-401-2"),
    UNEXPECTED_TOKEN(401, "Unexpected Token", "SECURITY-401-3"),
    INVALID_ROLE(401, "Invalid Role", "SECURITY-401-4"),

    FORBIDDEN(403, "Can Not Access", "SECURITY-403-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
