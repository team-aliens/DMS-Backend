package team.aliens.dms.domain.school.exception

import team.aliens.dms.domain.school.error.SchoolErrorCode
import team.aliens.dms.common.error.DmsException

object AnswerMismatchException : DmsException(
    SchoolErrorCode.ANSWER_MISMATCH
)