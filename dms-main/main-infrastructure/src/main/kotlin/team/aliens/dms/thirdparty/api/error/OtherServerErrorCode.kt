package team.aliens.dms.thirdparty.api.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class OtherServerErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    OTHER_SERVER_BAD_REQUEST(ErrorStatus.BAD_REQUEST, "Other Server Bad Request", 1),
    OTHER_SERVER_UNAUTHORIZED(ErrorStatus.UNAUTHORIZED, "Other Server Unauthorized", 1),
    OTHER_SERVER_FORBIDDEN(ErrorStatus.FORBIDDEN, "Other Server Forbidden", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "OTHER-$status-$sequence"
}
