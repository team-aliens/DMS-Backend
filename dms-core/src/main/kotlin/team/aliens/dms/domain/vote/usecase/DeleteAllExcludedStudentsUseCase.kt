package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.spi.ExcludedStudentPort

@UseCase
class DeleteAllExcludedStudentsUseCase (
    private val excludedStudentPort: ExcludedStudentPort
){
    fun execute() {
        excludedStudentPort.deleteAllExcludedStudents()
    }
}
