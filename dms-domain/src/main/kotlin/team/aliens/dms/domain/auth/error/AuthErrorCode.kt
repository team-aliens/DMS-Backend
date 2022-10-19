package team.aliens.dms.domain.auth.error

import team.aliens.dms.global.error.ErrorProperty

enum class AuthErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    AUTH_CODE_NOT_MATCHED(401, "Auth Code Not Matched"),
    AUTH_CODE_NOT_FOUND(404, "Auth Code Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}