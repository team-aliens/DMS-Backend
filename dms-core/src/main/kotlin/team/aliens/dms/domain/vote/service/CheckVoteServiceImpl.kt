package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.exception.ExcludedStudentAlreadyExistsException
import team.aliens.dms.domain.vote.exception.VoteTypeMismatchException
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.QueryExcludedStudentPort
import team.aliens.dms.domain.vote.spi.QueryVotePort
import team.aliens.dms.domain.vote.spi.QueryVotingOptionPort
import team.aliens.dms.domain.vote.spi.QueryVotingTopicPort
import java.util.UUID

@Service
class CheckVoteServiceImpl(
    private val queryVotePort: QueryVotePort,
    private val queryVotingTopicPort: QueryVotingTopicPort,
    private val queryVotingOptionPort: QueryVotingOptionPort,
    private val queryExcludedStudentPort: QueryExcludedStudentPort
) : CheckVoteService {

    override fun checkVotingTopicExistByName(name: String): Boolean = queryVotingTopicPort.existVotingTopicByName(name)

    override fun checkVotingTopicExistById(id: UUID): Boolean = queryVotingTopicPort.existVotingTopicById(id)

    override fun checkVotingOptionExistById(id: UUID): Boolean = queryVotingOptionPort.existVotingOptionById(id)

    override fun checkVoteExistByStudentIdAndVotingTopicId(studentId: UUID, votingTopicId: UUID): Boolean {
        queryVotePort.queryVoteByStudentId(studentId).forEach { vote ->
            if (vote.votingTopicId == votingTopicId) {
                return true
            }
        }
        return false
    }

    override fun checkTypeIsOptionVote(votingTopic: VotingTopic) {
        if (votingTopic.voteType != VoteType.OPTION_VOTE) {
            throw VoteTypeMismatchException
        }
    }

    override fun checkExcludedStudentExistByStudentId(studentId: UUID) {
        if (queryExcludedStudentPort.existExcludedStudentById(studentId)) {
            throw ExcludedStudentAlreadyExistsException
        }
    }
}
