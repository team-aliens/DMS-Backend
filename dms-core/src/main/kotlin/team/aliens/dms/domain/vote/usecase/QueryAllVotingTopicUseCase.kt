package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.vote.dto.response.VotingTopicResponse
import team.aliens.dms.domain.vote.dto.response.VotingTopicsResponse
import team.aliens.dms.domain.vote.service.VoteService

@ReadOnlyUseCase
class QueryAllVotingTopicUseCase(
    val voteService: VoteService,
    val studentService: StudentService
) {

    fun execute(): VotingTopicsResponse {
        val student = studentService.getCurrentStudent()
        val voteTopics = voteService.getAllVotingTopics(student.id)

        return VotingTopicsResponse(
            voteTopics.map {
                VotingTopicResponse.from(it)
            }
        )
    }
}
