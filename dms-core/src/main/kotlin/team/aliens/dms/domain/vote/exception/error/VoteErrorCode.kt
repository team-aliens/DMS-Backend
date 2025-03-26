package team.aliens.dms.domain.vote.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class VoteErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    VOTE_TYPE_MISMATCH(ErrorStatus.BAD_REQUEST, "Vote Type mismatch", 1),
    WRONG_VOTE_TYPE(ErrorStatus.BAD_REQUEST, "Wrong vote type", 2),
    WRONG_SCHOOL_VOTED(ErrorStatus.UNAUTHORIZED, "Wrong school voted", 1),
    UNAUTHORIZED_VOTE_DELETION(ErrorStatus.UNAUTHORIZED, "Unauthorized to delete this vote", 2),
    INVALID_PERIOD(ErrorStatus.FORBIDDEN, "Invalid Period", 1),
    INVALID_VOTING_PERIOD(ErrorStatus.FORBIDDEN, "Invalid Voting Period", 2),
    STUDENT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Student Not Found", 1),
    STUDENT_ID_NOT_FOUND(ErrorStatus.NOT_FOUND, "Student ID Not Found", 2),
    VOTING_TOPIC_NOT_FOUND(ErrorStatus.NOT_FOUND, "Voting topic not found", 3),
    VOTING_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Voting option not found", 4),
    VOTE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Vote not found", 5),
    ALREADY_VOTED(ErrorStatus.CONFLICT, "Already voted", 1),
    VOTING_TOPIC_NAME_ALREADY_EXIST(ErrorStatus.CONFLICT, "Voting Topic Already Exist", 2),
    EXCLUDED_STUDENT_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Excluded Student Already Exist", 3)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "VOTE-$status-$sequence"
}
