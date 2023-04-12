package team.aliens.dms.domain.school.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.school.exception.error.SchoolErrorCode

object FeatureNotAvailableException : DmsException(
    SchoolErrorCode.FEATURE_NOT_AVAILABLE
)
