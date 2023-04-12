package team.aliens.dms.thirdparty.api.error

import team.aliens.dms.common.error.ErrorProperty

enum class OtherServerErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {
    OTHER_SERVER_BAD_REQUEST(400, "Other Server Bad Request", 1),
    OTHER_SERVER_UNAUTHORIZED(401, "Other Server Unauthorized", 1),
    OTHER_SERVER_FORBIDDEN(403, "Other Server Forbidden", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "THIRD-PARTY-$status-$sequence"
}
