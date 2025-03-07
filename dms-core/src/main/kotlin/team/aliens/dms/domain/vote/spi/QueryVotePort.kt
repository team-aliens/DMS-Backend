package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import java.util.UUID

interface QueryVotePort {

    fun queryVoteById(voteId: UUID): Vote?

    fun queryStudentVotingByVotingTopicIdAndGrade(votingTopicId: UUID, grade: Int): List<StudentVotingResultVO>

    fun queryOptionVotingByVotingTopicId(votingTopicId: UUID): List<OptionVotingResultVO>

    fun queryVoteByStudentId(studentId: UUID): List<Vote>
}
