package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.vote.model.ModelStudent

object StudentNotFoundException : DmsException(
    VoteErrorCode.STUDENT_NOT_FOUND
)

object StudentIdNotFoundException : DmsException(
    VoteErrorCode.STUDENT_ID_NOT_FOUND
)

fun validateStudentList(modelStudentList: List<ModelStudent>) {
    if (modelStudentList.isEmpty()) {
        throw StudentNotFoundException
    }
}