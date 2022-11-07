package team.aliens.dms.domain.manager.exception

import team.aliens.dms.domain.manager.error.ManagerErrorCode
import team.aliens.dms.common.error.DmsException

object ManagerNotFoundException : DmsException(
    ManagerErrorCode.MANAGER_NOT_FOUND
)