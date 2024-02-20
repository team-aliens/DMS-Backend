package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.dto.StudentsResponse
import team.aliens.dms.domain.student.service.StudentService

@UseCase
class StudentGetAllStudentsUseCase(
    private val studentService: StudentService
) {

    fun execute(name: String?): StudentsResponse {
        val students = studentService.getAllStudentsByName(name)

        return StudentsResponse.of(students)
    }
}
