package team.aliens.dms.domain.remain.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.remain.error.RemainStatusErrorCode

object RemainStatusNotFound : DmsException(
    RemainStatusErrorCode.REMAIN_STATUS_NOT_FOUND
)