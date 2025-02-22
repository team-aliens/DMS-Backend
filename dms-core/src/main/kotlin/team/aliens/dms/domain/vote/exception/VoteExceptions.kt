package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.DmsException

object NotValidPeriodException : DmsException(
    VotingTopicErrorCode.NOT_VALID_PERIOD
)

object NotVotingPeriodException : DmsException(
    VotingTopicErrorCode.NOT_VOTING_PERIOD
)

object VotingTopicNotFoundException : DmsException(
    VotingTopicErrorCode.VOTING_TOPIC_NOT_FOUND
)
