package team.aliens.dms.global.security.exception

import team.aliens.dms.global.error.DmsException
import team.aliens.dms.global.security.error.SecurityErrorCode

object InvalidRoleException : DmsException(
    SecurityErrorCode.INVALID_ROLE
)