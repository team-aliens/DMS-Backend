package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.VotingOption
import java.util.UUID

interface QueryVotingOptionPort {

    fun queryVotingOptionById(votingOptionId: UUID): VotingOption?

    fun existVotingOptionById(votingOptionId: UUID): Boolean

    fun queryVotingOptionsByVotingTopicId(votingTopicId: UUID): List<VotingOption>?
}
