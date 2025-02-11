package team.aliens.dms.domain.vote.model

import team.aliens.dms.domain.vote.exception.VoteTypeMismatchException

enum class VoteType{
    OPTION_VOTE,
    STUDENT_VOTE,
    MODEL_STUDENT_VOTE,
    APPROVAL_VOTE
}