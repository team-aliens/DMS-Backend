package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class VoteErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    INVALID_PERIOD(ErrorStatus.BAD_REQUEST, "Invalid Period", 1),
    INVALID_VOTING_PERIOD(ErrorStatus.BAD_REQUEST, "Invalid Voting Period", 3),

    VOTING_TOPIC_NOT_FOUND(ErrorStatus.NOT_FOUND, "VotingTopic Not Found", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "VOTE-$status-$sequence"
}
