package team.aliens.dms.domain.auth.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class AuthErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    AUTH_CODE_MISMATCH(ErrorStatus.UNAUTHORIZED, "Auth Code Mismatch"),
    EMAIL_MISMATCH(ErrorStatus.UNAUTHORIZED, "Email Mismatch"),
    PASSWORD_MISMATCH(ErrorStatus.UNAUTHORIZED, "Password Mismatch"),

    REFRESH_TOKEN_NOT_FOUND(ErrorStatus.NOT_FOUND, "Refresh Token Not Found"),
    AUTH_CODE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Auth Code Not Found"),
    AUTH_CODE_LIMIT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Auth Code Limit Not Found"),

    EMAIL_ALREADY_CERTIFIED(ErrorStatus.CONFLICT, "Email Already Certified"),

    AUTH_CODE_OVER_LIMITED(ErrorStatus.TOO_MANY_REQUEST, "Auth Code Over Limited")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
