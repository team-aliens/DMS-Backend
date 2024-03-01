package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.dto.StudentsResponse
import team.aliens.dms.domain.student.service.StudentService

@ReadOnlyUseCase
class StudentGetAllStudentsUseCase(
    private val studentService: StudentService
) {

    fun execute(name: String?): StudentsResponse {
        val students = studentService.getAllStudentsByName(name)

        return StudentsResponse.of(students)
    }
}
