package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.AvailableTimeErrorCode

object AvailableTimeNotFoundException : DmsException(
    AvailableTimeErrorCode.AVAILABLE_TIME_NOT_FOUND
)
