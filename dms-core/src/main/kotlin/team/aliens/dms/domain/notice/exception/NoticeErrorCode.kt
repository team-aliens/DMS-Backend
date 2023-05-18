package team.aliens.dms.domain.notice.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class NoticeErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    IS_NOT_WRITER(ErrorStatus.UNAUTHORIZED, "Only Writer Can Delete", 1),

    NOTICE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Notice Not Found", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "NOTICE-$status-$sequence"
}
