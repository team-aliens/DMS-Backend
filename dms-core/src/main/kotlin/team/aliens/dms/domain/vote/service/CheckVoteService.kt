package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface CheckVoteService {

    fun checkVotingTopicExistByName(name: String): Boolean

    fun checkVotingTopicExistById(id: UUID): Boolean

    fun checkVotingOptionExistById(id: UUID): Boolean

    fun checkVoteExistByStudentIdAndVotingTopicId(studentId: UUID, votingTopicId: UUID): Boolean

    fun checkTypeIsOptionVote(votingTopic: VotingTopic)

    fun checkVoteDeletionAuthorization(vote: Vote, student: Student)
}
