package team.aliens.dms.domain.notice.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.notice.exception.error.NoticeErrorCode

object NoticeNotFoundException : DmsException(
    NoticeErrorCode.NOTICE_NOT_FOUND
)
