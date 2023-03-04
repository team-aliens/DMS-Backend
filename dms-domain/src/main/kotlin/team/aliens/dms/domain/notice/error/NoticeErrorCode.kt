package team.aliens.dms.domain.notice.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class NoticeErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    IS_NOT_WRITER(ErrorStatus.UNAUTHORIZED, "Only Writer Can Delete"),

    NOTICE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Notice Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
