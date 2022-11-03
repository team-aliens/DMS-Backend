package team.aliens.dms.domain.school.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.school.error.SchoolErrorCode

object SchoolInfoMismatchException : DmsException(
    SchoolErrorCode.SCHOOL_INFO_MISMATCH
)