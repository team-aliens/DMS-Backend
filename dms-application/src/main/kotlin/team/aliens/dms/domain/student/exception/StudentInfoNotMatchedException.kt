package team.aliens.dms.domain.student.exception

import team.aliens.dms.domain.student.error.StudentErrorCode
import team.aliens.dms.global.error.DmsException

object StudentInfoNotMatchedException : DmsException(
    StudentErrorCode.STUDENT_INFO_NOT_MATCHED
)