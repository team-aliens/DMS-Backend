package team.aliens.dms.domain.student.exception

import team.aliens.dms.common.error.DynamicDmsException
import team.aliens.dms.common.util.StringUtil.toStringWithoutBracket
import team.aliens.dms.domain.student.exception.error.StudentErrorCode

class StudentUpdateInfoNotFoundException(
    invalidStudentNames: List<String>,
) : DynamicDmsException(
    status = StudentErrorCode.STUDENT_UPDATE_INFO_NOT_FOUND.status(),
    message = StudentErrorCode.STUDENT_UPDATE_INFO_NOT_FOUND.message().format(
        invalidStudentNames.take(3).toStringWithoutBracket(),
        invalidStudentNames.size
    ),
    code = StudentErrorCode.STUDENT_UPDATE_INFO_NOT_FOUND.code()
)
