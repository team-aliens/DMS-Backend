package team.aliens.dms.domain.manager.exception

import team.aliens.dms.domain.manager.error.ManagerErrorCode
import team.aliens.dms.global.error.DmsException

object ManagerInfoNotMatchedException : DmsException(
    ManagerErrorCode.MANAGER_INFO_NOT_MATCHED
)