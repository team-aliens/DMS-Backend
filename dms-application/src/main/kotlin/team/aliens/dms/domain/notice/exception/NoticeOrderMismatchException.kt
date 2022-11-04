package team.aliens.dms.domain.notice.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.notice.error.NoticeErrorCode

object NoticeOrderMismatchException : DmsException(
    NoticeErrorCode.NOTICE_ORDER_MISMATCH
)