package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.service.VoteService
import java.util.UUID

@UseCase
class DeleteVotingOptionUseCase(
    private val voteService: VoteService
) {

    fun execute(votingOptionId: UUID) {
        val votingOption: VotingOption = voteService.getVotingOptionById(votingOptionId)

        val voteType: VoteType = voteService.getVotingTopicById(votingOption.votingTopicId).voteType

        if (voteType == VoteType.APPROVAL_VOTE || voteType == VoteType.OPTION_VOTE) {
            voteService.deleteVoteByVotingOption(votingOption)
        }

        voteService.deleteVotingOptionById(votingOption.id)
    }
}
