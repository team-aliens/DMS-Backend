package team.aliens.dms.domain.student.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.student.error.StudentErrorCode

object StudentInfoMismatchException : DmsException(
    StudentErrorCode.STUDENT_INFO_MISMATCH
)
