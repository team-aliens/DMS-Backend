package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.vote.dto.response.VoteResponse
import team.aliens.dms.domain.vote.dto.response.VotesResponse
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.VoteService
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import java.util.UUID

@ReadOnlyUseCase
class QueryVotesUseCase(
    private val voteService: VoteService,
    private val studentService: StudentService
) {

    fun execute(votingTopicId: UUID): VotesResponse {
        val votingTopic = voteService.getVotingTopicById(votingTopicId)

        return when (votingTopic.voteType) {
            VoteType.STUDENT_VOTE -> queryStudentVotingVotes(votingTopic, false)
            VoteType.MODEL_STUDENT_VOTE -> queryStudentVotingVotes(votingTopic, true)
            VoteType.OPTION_VOTE, VoteType.APPROVAL_VOTE -> queryOptionVotingVotes(votingTopic)
        }
    }

    private fun queryOptionVotingVotes(votingTopic: VotingTopic): VotesResponse {
        val result: List<OptionVotingResultVO> = voteService.getVotesInOptionVotingByVotingTopicId(votingTopic.id)
        return VotesResponse.of(
            votingTopicName = votingTopic.topicName,
            votes = result.map { votingResult ->
                VoteResponse(
                    id = votingResult.id,
                    name = votingResult.name,
                    votes = votingResult.votes,
                    classNumber = null
                )
            }
        )
    }

    private fun queryStudentVotingVotes(votingTopic: VotingTopic, isModelStudent: Boolean): VotesResponse {
        return VotesResponse.of(
                votingTopicName = votingTopic.topicName,
            votes = listOf(1, 2, 3).flatMap { grade ->

                val result: List<StudentVotingResultVO> =
                    if (isModelStudent) voteService.getVotesInModelStudentVotingByVotingTopicId(votingTopic.id, grade)
                    else voteService.getVotesInStudentVotingByVotingTopicId(votingTopic.id, grade)

                result.map { votingResult ->
                    val student = studentService.getStudentById(votingResult.id)
                    val classNumber = Student.processGcn(
                        grade = student.grade,
                        classRoom = student.classRoom,
                        number = student.number
                    )

                    VoteResponse.of(
                        id = votingResult.id,
                        name = votingResult.name,
                        votes = votingResult.votes,
                        classNumber = classNumber
                    )
                }
            }
        )
    }
}
