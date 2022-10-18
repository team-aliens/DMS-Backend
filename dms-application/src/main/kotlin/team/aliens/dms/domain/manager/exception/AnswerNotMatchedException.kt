package team.aliens.dms.domain.manager.exception

import team.aliens.dms.domain.manager.error.ManagerErrorCode
import team.aliens.dms.global.error.DmsException

object AnswerNotMatchedException : DmsException(
    ManagerErrorCode.ANSWER_NOT_MATCHED
)