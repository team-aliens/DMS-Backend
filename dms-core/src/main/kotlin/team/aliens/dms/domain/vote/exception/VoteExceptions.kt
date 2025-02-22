package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.DmsException

object InvalidPeriodException : DmsException(
    VoteErrorCode.INVALID_PERIOD
)

object InvalidVotingPeriodException : DmsException(
    VoteErrorCode.INVALID_VOTING_PERIOD
)

object VotingTopicNotFoundException : DmsException(
    VoteErrorCode.VOTING_TOPIC_NOT_FOUND
)
