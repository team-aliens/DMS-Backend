package team.aliens.dms.thirdparty.email.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.global.error.GlobalErrorCode

object SesException : DmsException(
    GlobalErrorCode.SIMPLE_EMAIL_SERVICE
)
