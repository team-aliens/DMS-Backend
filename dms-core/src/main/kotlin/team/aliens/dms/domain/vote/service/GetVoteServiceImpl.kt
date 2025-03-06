package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.exception.VoteNotFoundException
import team.aliens.dms.domain.vote.exception.VotingOptionNotFoundException
import team.aliens.dms.domain.vote.exception.VotingTopicNotFoundException
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.QueryVotePort
import team.aliens.dms.domain.vote.spi.QueryVotingOptionPort
import team.aliens.dms.domain.vote.spi.QueryVotingTopicPort
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import java.util.UUID

@Service
class GetVoteServiceImpl(
    private val queryVotePort: QueryVotePort,
    private val queryVotingTopicPort: QueryVotingTopicPort,
    private val queryVotingOptionPort: QueryVotingOptionPort
) : GetVoteService {

    override fun getVotingTopicById(votingTopicId: UUID): VotingTopic {
        return queryVotingTopicPort.queryVotingTopicById(votingTopicId) ?: throw VotingTopicNotFoundException
    }

    override fun getAllVotingTopics(): List<VotingTopic> {
        return queryVotingTopicPort.queryAllVotingTopic()
    }

    override fun getVotingOptionsByVotingTopicId(votingTopicId: UUID): List<VotingOption>? = queryVotingOptionPort.queryVotingOptionsByVotingTopicId(votingTopicId)

    override fun getVotesInStudentVotingByVotingTopicId(votingTopicId: UUID, grade: Int): List<StudentVotingResultVO> {
        return queryVotePort.queryStudentVotingByVotingTopicIdAndGrade(votingTopicId, grade)
    }

    override fun getVotesInOptionVotingByVotingTopicId(votingTopicId: UUID): List<OptionVotingResultVO> {
        return queryVotePort.queryOptionVotingByVotingTopicId(votingTopicId)
    }

    override fun getVotingOptionById(votingOptionId: UUID): VotingOption {
        return queryVotingOptionPort.queryVotingOptionById(votingOptionId) ?: throw VotingOptionNotFoundException
    }

    override fun getVoteById(voteId: UUID): Vote {
        return queryVotePort.queryVoteById(voteId) ?: throw VoteNotFoundException
    }
}
