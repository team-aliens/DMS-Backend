package team.aliens.dms.thirdparty.email.exception

import team.aliens.dms.global.error.DmsException
import team.aliens.dms.global.error.GlobalErrorCode

object SendEmailRejectedException : DmsException(
    GlobalErrorCode.INTERNAL_SERVER_ERROR
)