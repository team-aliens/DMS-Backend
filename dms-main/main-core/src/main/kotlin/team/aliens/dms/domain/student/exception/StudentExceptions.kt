package team.aliens.dms.domain.student.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.common.util.StringUtil.toStringWithoutBracket
import team.aliens.dms.domain.student.exception.error.StudentErrorCode

object SexMismatchException : DmsException(
    StudentErrorCode.SEX_MISMATCH
)

object StudentAlreadyExistsException : DmsException(
    StudentErrorCode.STUDENT_ALREADY_EXISTS
)

object StudentInfoMismatchException : DmsException(
    StudentErrorCode.STUDENT_INFO_MISMATCH
)

object StudentNotFoundException : DmsException(
    StudentErrorCode.STUDENT_NOT_FOUND
)

class StudentUpdateInfoNotFoundException(
    invalidStudentNames: List<String>,
) : DmsException(
    StudentErrorCode.STUDENT_UPDATE_INFO_NOT_FOUND
        .formatMessage(
            invalidStudentNames.take(3).toStringWithoutBracket(),
            invalidStudentNames.size.toString()
        )
)
