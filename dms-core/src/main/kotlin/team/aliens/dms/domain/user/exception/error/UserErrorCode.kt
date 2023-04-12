package team.aliens.dms.domain.user.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class UserErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    INVALID_ROLE(ErrorStatus.FORBIDDEN, "Invalid Role", 1),

    USER_NOT_FOUND(ErrorStatus.NOT_FOUND, "User Not Found", 1),

    USER_EMAIL_EXISTS(ErrorStatus.CONFLICT, "User Email Exists", 1),
    USER_ACCOUNT_ID_EXISTS(ErrorStatus.CONFLICT, "User Account Id Exists", 2)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "USER-$status-$sequence"
}
