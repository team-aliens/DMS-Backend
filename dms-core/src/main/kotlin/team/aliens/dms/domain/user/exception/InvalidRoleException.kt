package team.aliens.dms.domain.user.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.user.exception.error.UserErrorCode

object InvalidRoleException : DmsException(
    UserErrorCode.INVALID_ROLE
)
