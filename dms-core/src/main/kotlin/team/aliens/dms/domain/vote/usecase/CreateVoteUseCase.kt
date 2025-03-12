package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.vote.exception.AlreadyVotedException
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.VoteService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class CreateVoteUseCase(
    private val voteService: VoteService,
    private val studentService: StudentService
) {

    fun execute(selectedId: UUID, votingTopicId: UUID) {
        val student = studentService.getCurrentStudent()
        val votingTopic: VotingTopic = voteService.getVotingTopicById(votingTopicId)

        if (voteService.checkVoteExistByStudentIdAndVotingTopicId(student.id, votingTopicId)) {
            throw AlreadyVotedException
        }

        val voteType: VoteType = votingTopic.voteType
        
        val selectedOptionId = if (voteType == VoteType.OPTION_VOTE || voteType == VoteType.APPROVAL_VOTE) {
            voteService.getVotingOptionById(selectedId).id
        } else {
            null
        }
        val selectedStudentId = if (voteType == VoteType.STUDENT_VOTE || voteType == VoteType.MODEL_STUDENT_VOTE) {
            studentService.getStudentById(selectedId).id
        } else {
            null
        }

        voteService.createVote(
                Vote(
                        studentId = student.id,
                        votingTopicId = votingTopic.id,
                        votedAt = LocalDateTime.now(),
                        selectedOptionId = selectedOptionId,
                        selectedStudentId = selectedStudentId
                )
        )
    }
}
