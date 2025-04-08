package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.vote.dto.response.VoteResponse
import team.aliens.dms.domain.vote.dto.response.VotesResponse
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.service.VoteService
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import java.util.UUID

@UseCase
class QueryVotesUseCase(
    private val voteService: VoteService,
    private val studentService: StudentService
) {

    fun execute(votingTopicId: UUID): VotesResponse {
        val votingTopic = voteService.getVotingTopicById(votingTopicId)

        return when (votingTopic.voteType) {
            VoteType.STUDENT_VOTE -> queryStudentVotingVotes(votingTopicId, false)
            VoteType.MODEL_STUDENT_VOTE -> queryStudentVotingVotes(votingTopicId, true)
            VoteType.OPTION_VOTE, VoteType.APPROVAL_VOTE -> queryOptionVotingVotes(votingTopicId)
        }
    }

    private fun queryOptionVotingVotes(votingTopicId: UUID): VotesResponse {
        val result: List<OptionVotingResultVO> = voteService.getVotesInOptionVotingByVotingTopicId(votingTopicId)
        return VotesResponse.of(
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

    private fun queryStudentVotingVotes(votingTopicId: UUID, isModelStudent: Boolean): VotesResponse {
        return VotesResponse.of(
            listOf(1, 2, 3).flatMap { grade ->

                val result: List<StudentVotingResultVO> =
                    if (isModelStudent) voteService.getVotesInModelStudentVotingByVotingTopicId(votingTopicId, grade)
                    else voteService.getVotesInStudentVotingByVotingTopicId(votingTopicId, grade)

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
