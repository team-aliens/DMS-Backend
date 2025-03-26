package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.model.ExcludedStudent
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import java.util.UUID

interface GetVoteService {

    fun getVotingTopicById(votingTopicId: UUID): VotingTopic

    fun getAllVotingTopics(): List<VotingTopic>

    fun getVotingOptionsByVotingTopicId(votingTopicId: UUID): List<VotingOption>

    fun getVotesInStudentVotingByVotingTopicId(votingTopicId: UUID, grade: Int): List<StudentVotingResultVO>

    fun getVotesInOptionVotingByVotingTopicId(votingTopicId: UUID): List<OptionVotingResultVO>

    fun getVotingOptionById(votingOptionId: UUID): VotingOption

    fun getVoteById(voteId: UUID): Vote

    fun getAllExcludedStudents(): List<ExcludedStudent>

    fun getExcludedStudentById(excludedStudentId: UUID): ExcludedStudent
}
