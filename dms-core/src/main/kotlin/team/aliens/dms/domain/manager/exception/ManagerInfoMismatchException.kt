package team.aliens.dms.domain.manager.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.manager.exception.error.ManagerErrorCode

object ManagerInfoMismatchException : DmsException(
    ManagerErrorCode.MANAGER_INFO_NOT_MATCHED
)
