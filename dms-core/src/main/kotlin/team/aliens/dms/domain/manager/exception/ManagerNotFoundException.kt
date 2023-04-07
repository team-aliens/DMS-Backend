package team.aliens.dms.domain.manager.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.manager.exception.error.ManagerErrorCode

object ManagerNotFoundException : DmsException(
    ManagerErrorCode.MANAGER_NOT_FOUND
)
