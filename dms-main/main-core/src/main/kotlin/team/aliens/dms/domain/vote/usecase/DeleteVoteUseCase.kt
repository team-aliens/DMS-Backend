package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.vote.exception.IsNotAuthorizedVoteDeletionException
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.service.VoteService
import java.util.UUID

@UseCase
class DeleteVoteUseCase(
    private val voteService: VoteService,
    private val studentService: StudentService
) {

    fun execute(voteId: UUID) {
        val student: Student = studentService.getCurrentStudent()
        val vote: Vote = voteService.getVoteById(voteId)

        if (student.id != vote.studentId) {
            throw IsNotAuthorizedVoteDeletionException
        }

        voteService.deleteVoteById(vote.id)
    }
}
