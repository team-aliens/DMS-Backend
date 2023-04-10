package team.aliens.dms.domain.notice.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class NoticeErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    IS_NOT_WRITER(ErrorStatus.UNAUTHORIZED, "Only Writer Can Delete", "NOTICE-401-1"),

    NOTICE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Notice Not Found", "NOTICE-404-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
