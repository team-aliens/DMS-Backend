package team.aliens.dms.domain.vote.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class VoteErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    VOTE_TYPE_MISMATCH(ErrorStatus.BAD_REQUEST, "Vote Type mismatch", 1),
    VOTING_TOPIC_NAME_ALREADY_EXIST(ErrorStatus.CONFLICT, "Voting Topic Already Exist", 1),
    VOTING_TOPIC_NOT_FOUND(ErrorStatus.NOT_FOUND, "Voting topic not found", 1),
    WRONG_VOTE_TYPE(ErrorStatus.BAD_REQUEST, "Wrong vote type", 1),
    VOTING_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Voting option not found", 1),
    VOTE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Vote not found", 1),
    ALREADY_VOTED(ErrorStatus.CONFLICT, "Already voted", 1),
    WRONG_SCHOOL_VOTED(ErrorStatus.UNAUTHORIZED, "Wrong school voted", 1),
    STUDENT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Student Not Found", 1),
    STUDENT_ID_NOT_FOUND(ErrorStatus.NOT_FOUND, "Student ID Not Found", 2),
    INVALID_PERIOD(ErrorStatus.FORBIDDEN, "Invalid Period", 3),
    INVALID_VOTING_PERIOD(ErrorStatus.FORBIDDEN, "Invalid Voting Period", 4),
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "VOTE-$status-$sequence"
}
