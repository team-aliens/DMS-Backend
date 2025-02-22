package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class VotingTopicErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    NOT_VALID_PERIOD(ErrorStatus.BAD_REQUEST, "Not Valid Period", 1),
    VOTING_TOPIC_NOT_FOUND(ErrorStatus.BAD_REQUEST, "VotingTopic Not Found", 2),
    NOT_VOTING_PERIOD(ErrorStatus.BAD_REQUEST, "Not Voting Period", 3),
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "VOTE-$status-$sequence"
}
