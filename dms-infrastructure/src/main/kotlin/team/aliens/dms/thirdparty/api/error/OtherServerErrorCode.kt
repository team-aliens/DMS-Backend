package team.aliens.dms.thirdparty.api.error

import team.aliens.dms.common.error.ErrorProperty

enum class OtherServerErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {
    OTHER_SERVER_BAD_REQUEST(400, "Other Server Bad Request", "THIRD-PARTY-400-1"),
    OTHER_SERVER_UNAUTHORIZED(401, "Other Server Unauthorized", "THIRD-PARTY-401-1"),
    OTHER_SERVER_FORBIDDEN(403, "Other Server Forbidden", "THIRD-PARTY-403-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
