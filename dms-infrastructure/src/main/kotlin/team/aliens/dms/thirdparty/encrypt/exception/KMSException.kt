package team.aliens.dms.thirdparty.encrypt.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.global.error.GlobalErrorCode

object KMSException : DmsException(
    GlobalErrorCode.KEY_MANAGEMENT_SERVICE
)
