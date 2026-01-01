package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.VoteService
import java.util.UUID

@UseCase
class DeleteVotingTopicUseCase(
    private val voteService: VoteService,
    private val noticeService: NoticeService
) {

    fun execute(votingTopicId: UUID) {
        val votingTopic: VotingTopic = voteService.getVotingTopicById(votingTopicId)

        voteService.deleteVoteByVotingTopic(votingTopic)

        votingTopic.voteType
            .takeIf { it == VoteType.APPROVAL_VOTE || it == VoteType.OPTION_VOTE }
            .let { voteService.deleteVotingOptionByVotingTopic(votingTopic) }

        voteService.deleteVotingTopicById(votingTopicId)
        noticeService.cancelVoteResultNotice(votingTopicId)
    }
}
