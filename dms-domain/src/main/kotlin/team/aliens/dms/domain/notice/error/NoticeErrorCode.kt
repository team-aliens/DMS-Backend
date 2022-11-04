package team.aliens.dms.domain.notice.error

import team.aliens.dms.common.error.ErrorProperty

enum class NoticeErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    IS_NOT_WRITER(401, "Only Writer Can Delete"),
    NOTICE_ORDER_MISMATCH(401, "Notice Order Mismatch"),

    NOTICE_NOT_FOUND(404, "Notice Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}