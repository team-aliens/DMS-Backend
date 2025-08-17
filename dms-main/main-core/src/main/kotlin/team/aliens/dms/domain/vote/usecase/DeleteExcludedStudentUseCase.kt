package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.model.ExcludedStudent
import team.aliens.dms.domain.vote.service.VoteService
import java.util.UUID

@UseCase
class DeleteExcludedStudentUseCase(
    private val voteService: VoteService
) {

    fun execute(excludedStudentId: UUID) {
        val excludedStudent: ExcludedStudent = voteService.getExcludedStudentById(excludedStudentId)
        voteService.deleteExcludedStudentById(excludedStudent.studentId)
    }
}
