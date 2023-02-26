package team.aliens.dms.domain.auth.error

import team.aliens.dms.common.error.ErrorProperty

enum class AuthErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    AUTH_CODE_MISMATCH(401, "Auth Code Mismatch"),
    EMAIL_MISMATCH(401, "Email Mismatch"),
    PASSWORD_MISMATCH(401, "Password Mismatch"),

    REFRESH_TOKEN_NOT_FOUND(404, "Refresh Token Not Found"),
    AUTH_CODE_NOT_FOUND(404, "Auth Code Not Found"),
    AUTH_CODE_LIMIT_NOT_FOUND(404, "Auth Code Limit Not Found"),

    EMAIL_ALREADY_CERTIFIED(409, "Email Already Certified"),

    AUTH_CODE_OVER_LIMITED(429, "Auth Code Over Limited")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
