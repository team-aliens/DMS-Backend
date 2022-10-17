package team.aliens.dms.domain.school.exception

import team.aliens.dms.domain.school.error.SchoolErrorCode
import team.aliens.dms.global.error.DmsException

object SchoolNotFoundException : DmsException(
    SchoolErrorCode.SCHOOL_NOT_FOUND
)