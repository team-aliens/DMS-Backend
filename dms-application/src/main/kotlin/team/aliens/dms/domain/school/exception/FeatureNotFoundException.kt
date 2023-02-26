package team.aliens.dms.domain.school.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.school.error.SchoolErrorCode

object FeatureNotFoundException : DmsException(
    SchoolErrorCode.FEATURE_NOT_FOUND
)
