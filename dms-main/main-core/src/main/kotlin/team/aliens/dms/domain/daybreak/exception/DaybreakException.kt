package team.aliens.dms.domain.daybreak.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.daybreak.exception.error.DaybreakErrorCode

object DaybreakStudyApplicationAlreadyExistsException : DmsException(
    DaybreakErrorCode.EXIST_DAYBREAK_STUDY_APPLICATION
)

object DaybreakStudyApplicationNotFoundException : DmsException(
    DaybreakErrorCode.NOT_FOUND_DAYBREAK_STUDY_APPLICATION,
)

object DaybreakStudyTypeAlreadyExistsException : DmsException(
    DaybreakErrorCode.EXIST_DAYBREAK_STUDY_TYPE
)

object DaybreakStudyTypeNotFoundException : DmsException(
    DaybreakErrorCode.NOT_FOUND_DAYBREAK_STUDY_TYPE
)

object DaybreakStartDateAfterEndDateException : DmsException(
    DaybreakErrorCode.DAYBREAK_START_DATE_AFTER_END_DATE
)

object DaybreakPastDateException : DmsException(
    DaybreakErrorCode.DAYBREAK_PAST_DATE
)

object DaybreakInvalidDateRangeException : DmsException(
    DaybreakErrorCode.DAYBREAK_INVALID_DATE_RANGE
)
