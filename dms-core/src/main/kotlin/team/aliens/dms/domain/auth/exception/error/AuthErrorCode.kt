package team.aliens.dms.domain.auth.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class AuthErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    AUTH_CODE_MISMATCH(ErrorStatus.UNAUTHORIZED, "Auth Code Mismatch", "AUTH-401-1"),
    EMAIL_MISMATCH(ErrorStatus.UNAUTHORIZED, "Email Mismatch", "AUTH-401-2"),
    PASSWORD_MISMATCH(ErrorStatus.UNAUTHORIZED, "Password Mismatch", "AUTH-401-3"),
    UNVERIFIED_AUTH_CODE(ErrorStatus.UNAUTHORIZED, "Unverified Auth Code", "AUTH-401-4"),

    REFRESH_TOKEN_NOT_FOUND(ErrorStatus.NOT_FOUND, "Refresh Token Not Found", "AUTH-404-1"),
    AUTH_CODE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Auth Code Not Found", "AUTH-404-2"),
    AUTH_CODE_LIMIT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Auth Code Limit Not Found", "AUTH-404-3"),

    EMAIL_ALREADY_CERTIFIED(ErrorStatus.CONFLICT, "Email Already Certified", "AUTH-409-1"),

    AUTH_CODE_OVER_LIMITED(ErrorStatus.TOO_MANY_REQUEST, "Auth Code Over Limited", "AUTH-429-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
