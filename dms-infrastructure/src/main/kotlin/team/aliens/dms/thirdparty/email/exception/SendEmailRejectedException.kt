package team.aliens.dms.thirdparty.email.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.global.error.GlobalErrorCode

object SendEmailRejectedException : DmsException(
    GlobalErrorCode.SEND_EMAIL_REJECTED
)
