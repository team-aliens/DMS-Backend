package team.aliens.dms.global.security.error

import team.aliens.dms.global.error.ErrorProperty

enum class SecurityErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    INVALID_TOKEN(401, "Invalid Token"),
    EXPIRED_TOKEN(401, "Expired Token"),
    UNEXPECTED_TOKEN(401, "Unexpected Token"),
    INVALID_ROLE(401, "Invalid Role")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}