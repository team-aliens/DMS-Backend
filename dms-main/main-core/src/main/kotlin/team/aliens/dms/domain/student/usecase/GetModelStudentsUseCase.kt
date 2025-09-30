package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import team.aliens.dms.domain.vote.dto.response.ModelStudentsResponse
import java.time.LocalDate

@ReadOnlyUseCase
class GetModelStudentsUseCase(
    private val studentService: StudentService,
    private val userService: UserService
) {
    fun execute(date: LocalDate): ModelStudentsResponse {
        val user = userService.getCurrentUser()
        val modelStudents = studentService.getModelStudents(date, user.schoolId)

        return ModelStudentsResponse.of(modelStudents)
    }
}
