package team.aliens.dms.global.security.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.global.security.exception.error.SecurityErrorCode

object InvalidRoleException : DmsException(
    SecurityErrorCode.INVALID_ROLE
)
