package team.aliens.dms.domain.student.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.student.exception.error.StudentErrorCode

object StudentNotFoundException : DmsException(
    StudentErrorCode.STUDENT_NOT_FOUND
)
