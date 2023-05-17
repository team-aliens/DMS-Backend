package team.aliens.dms.domain.manager.exception

import team.aliens.dms.common.error.DmsException

object ManagerInfoMismatchException : DmsException(
    ManagerErrorCode.MANAGER_INFO_NOT_MATCHED
)
