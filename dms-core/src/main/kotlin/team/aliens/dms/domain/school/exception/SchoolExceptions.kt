package team.aliens.dms.domain.school.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.school.exception.error.SchoolErrorCode

object AnswerMismatchException : DmsException(
    SchoolErrorCode.ANSWER_MISMATCH
)

object FeatureNotAvailableException : DmsException(
    SchoolErrorCode.FEATURE_NOT_AVAILABLE
)

object FeatureNotFoundException : DmsException(
    SchoolErrorCode.FEATURE_NOT_FOUND
)

object SchoolCodeMismatchException : DmsException(
    SchoolErrorCode.SCHOOL_CODE_MISMATCH
)

object SchoolMismatchException : DmsException(
    SchoolErrorCode.SCHOOL_MISMATCH
)

object SchoolNotFoundException : DmsException(
    SchoolErrorCode.SCHOOL_NOT_FOUND
)

object ApplicationAvailableTimeNotFoundException : DmsException(
    SchoolErrorCode.APPLICATION_AVAILABLE_TIME_NOT_FOUND
)
