package team.aliens.dms.domain.user.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class UserErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    INVALID_ROLE(ErrorStatus.FORBIDDEN, "Invalid Role"),

    USER_NOT_FOUND(ErrorStatus.NOT_FOUND, "User Not Found"),

    USER_EMAIL_EXISTS(ErrorStatus.CONFLICT, "User Email Exists"),
    USER_ACCOUNT_ID_EXISTS(ErrorStatus.CONFLICT, "User Account Id Exists")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
