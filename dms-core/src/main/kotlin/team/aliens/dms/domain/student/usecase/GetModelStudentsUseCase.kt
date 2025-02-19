package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.vote.dto.response.ModelStudentResponse
import java.time.LocalDate

@ReadOnlyUseCase
class GetModelStudentsUseCase(
    private val StudentService: StudentService
) {
    fun execute(date: LocalDate): List<ModelStudentResponse> {
        return StudentService.getModelStudentList(date)
    }

}
