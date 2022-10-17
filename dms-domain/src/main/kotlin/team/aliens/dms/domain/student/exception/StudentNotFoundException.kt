package team.aliens.dms.domain.student.exception

import team.aliens.dms.domain.student.error.StudentErrorCode
import team.aliens.dms.global.error.DmsException

object StudentNotFoundException : DmsException(
    StudentErrorCode.STUDENT_NOT_FOUND
)