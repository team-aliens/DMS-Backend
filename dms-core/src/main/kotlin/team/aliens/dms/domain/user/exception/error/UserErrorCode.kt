package team.aliens.dms.domain.user.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class UserErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    INVALID_ROLE(ErrorStatus.FORBIDDEN, "Invalid Role", "USER-403-1"),

    USER_NOT_FOUND(ErrorStatus.NOT_FOUND, "User Not Found", "USER-404-1"),

    USER_EMAIL_EXISTS(ErrorStatus.CONFLICT, "User Email Exists", "USER-409-1"),
    USER_ACCOUNT_ID_EXISTS(ErrorStatus.CONFLICT, "User Account Id Exists", "USER-409-2")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
