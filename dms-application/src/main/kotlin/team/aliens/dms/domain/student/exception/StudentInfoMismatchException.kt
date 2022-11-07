package team.aliens.dms.domain.student.exception

import team.aliens.dms.domain.student.error.StudentErrorCode
import team.aliens.dms.common.error.DmsException

object StudentInfoMismatchException : DmsException(
    StudentErrorCode.STUDENT_INFO_MISMATCH
)