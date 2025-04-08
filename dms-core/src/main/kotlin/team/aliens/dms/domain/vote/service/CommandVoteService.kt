package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.model.ExcludedStudent
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface CommandVoteService {

    fun saveVotingTopic(voteTopic: VotingTopic): VotingTopic

    fun deleteVotingTopicById(votingTopicId: UUID)

    fun createVotingTopic(votingTopic: VotingTopic): VotingTopic

    fun createVote(vote: Vote): Vote

    fun createVotingOption(votingOption: VotingOption): VotingOption

    fun deleteVotingTopic(votingTopicId: UUID)

    fun deleteVotingOptionById(votingOptionId: UUID)

    fun deleteVoteById(voteId: UUID)

    fun checkVotingTopic(name: String): Boolean

    fun saveExcludedStudent(excludedStudent: ExcludedStudent): ExcludedStudent

    fun deleteExcludedStudentById(excludedStudentId: UUID)

    fun clearExcludedStudent()
}
