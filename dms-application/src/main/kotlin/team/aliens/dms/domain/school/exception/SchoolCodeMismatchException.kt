package team.aliens.dms.domain.school.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.school.error.SchoolErrorCode

object SchoolCodeMismatchException : DmsException(
    SchoolErrorCode.SCHOOL_CODE_MISMATCH
)