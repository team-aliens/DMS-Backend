package team.aliens.dms.domain.student.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.student.error.StudentErrorCode

object SexMismatchException : DmsException(
    StudentErrorCode.SEX_MISMATCH
)
