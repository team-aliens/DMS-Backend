package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.vote.exception.error.VoteErrorCode

object InvalidPeriodException : DmsException(
    VoteErrorCode.INVALID_PERIOD
)

object InvalidVotingPeriodException : DmsException(
    VoteErrorCode.INVALID_VOTING_PERIOD
)

object VotingTopicNotFoundException : DmsException(
    VoteErrorCode.VOTING_TOPIC_NOT_FOUND
)

object VoteTypeMismatchException : DmsException(
    VoteErrorCode.VOTE_TYPE_MISMATCH
)

object VotingTopicAlreadyExistException : DmsException(
    VoteErrorCode.VOTING_TOPIC_NAME_ALREADY_EXIST
)

object VotingOptionNotFoundException : DmsException(
    VoteErrorCode.VOTING_OPTION_NOT_FOUND
)

object WrongVoteTypeException : DmsException(
    VoteErrorCode.WRONG_VOTE_TYPE
)

object VoteNotFoundException : DmsException(
    VoteErrorCode.VOTE_NOT_FOUND
)

object AlreadyVotedException : DmsException(
    VoteErrorCode.ALREADY_VOTED
)
