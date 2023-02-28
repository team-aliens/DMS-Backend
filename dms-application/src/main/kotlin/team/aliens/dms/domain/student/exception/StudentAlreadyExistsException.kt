package team.aliens.dms.domain.student.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.student.error.StudentErrorCode

object StudentAlreadyExistsException : DmsException(
    StudentErrorCode.STUDENT_ALREADY_EXISTS
)
