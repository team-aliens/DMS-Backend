package team.aliens.dms.thirdparty.api.error

import team.aliens.dms.common.error.ErrorProperty

enum class OtherServerErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {
    OTHER_SERVER_BAD_REQUEST(400, "Other Server Bad Request"),
    OTHER_SERVER_UNAUTHORIZED(401, "Other Server Unauthorized"),
    OTHER_SERVER_FORBIDDEN(403, "Other Server Forbidden")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
