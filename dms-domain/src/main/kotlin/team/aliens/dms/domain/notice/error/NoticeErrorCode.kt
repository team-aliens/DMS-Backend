package team.aliens.dms.domain.notice.error

import team.aliens.dms.common.error.ErrorProperty

enum class NoticeErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    NOTICE_ORDER_MISMATCH(401, "Notice Order Mismatch")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}