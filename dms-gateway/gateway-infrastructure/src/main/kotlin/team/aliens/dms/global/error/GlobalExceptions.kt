package team.aliens.dms.global.error

import team.aliens.dms.common.error.DmsException

object InternalServerError : DmsException(
    GlobalErrorCode.INTERNAL_SERVER_ERROR
)
