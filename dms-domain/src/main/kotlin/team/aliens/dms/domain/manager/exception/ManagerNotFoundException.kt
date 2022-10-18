package team.aliens.dms.domain.manager.exception

import team.aliens.dms.domain.user.error.UserErrorCode
import team.aliens.dms.global.error.DmsException

object ManagerNotFoundException : DmsException(
    UserErrorCode.USER_NOT_FOUND
)