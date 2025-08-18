package team.aliens.dms.thirdparty.email.exception

import team.aliens.dms.common.error.DmsException

object SendEmailRejectedException : DmsException(
    MailErrorCode.SEND_EMAIL_REJECTED
)

object SesException : DmsException(
    MailErrorCode.SIMPLE_EMAIL_SERVICE
)
