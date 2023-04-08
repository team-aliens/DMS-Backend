package team.aliens.dms.domain.student.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.student.exception.error.VerifiedStudentErrorCode

object VerifiedStudentNotFoundException : DmsException(
    VerifiedStudentErrorCode.VERIFIED_STUDENT_NOT_FOUND
)
