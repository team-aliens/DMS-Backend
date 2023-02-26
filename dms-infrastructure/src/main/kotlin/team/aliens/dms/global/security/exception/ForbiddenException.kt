package team.aliens.dms.global.security.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.global.security.error.SecurityErrorCode

object ForbiddenException : DmsException(
    SecurityErrorCode.FORBIDDEN
)
