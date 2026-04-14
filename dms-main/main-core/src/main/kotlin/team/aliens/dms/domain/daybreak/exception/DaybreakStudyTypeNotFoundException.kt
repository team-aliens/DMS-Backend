package team.aliens.dms.domain.daybreak.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.daybreak.exception.error.DaybreakErrorCode

object DaybreakStudyTypeNotFoundException : DmsException(
    DaybreakErrorCode.NOT_FOUND_DAYBREAK_STUDY_TYPE
)
