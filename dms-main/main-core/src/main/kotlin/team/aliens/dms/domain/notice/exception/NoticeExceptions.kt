package team.aliens.dms.domain.notice.exception

import team.aliens.dms.common.error.DmsException

object NoticeNotFoundException : DmsException(
    NoticeErrorCode.NOTICE_NOT_FOUND
)

object IsNotWriterException : DmsException(
    NoticeErrorCode.IS_NOT_WRITER
)
