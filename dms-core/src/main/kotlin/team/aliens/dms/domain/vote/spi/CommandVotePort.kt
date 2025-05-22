package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface CommandVotePort {

    fun saveVote(vote: Vote): Vote

    fun deleteVoteById(voteId: UUID)

    fun deleteVotingOptionByVotingTopic(votingTopic: VotingTopic)

    fun deleteVoteByVotingTopic(votingTopic: VotingTopic)
}
