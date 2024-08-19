package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.dto.StudentsResponse
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class StudentGetAllStudentsUseCase(
    private val studentService: StudentService,
    private val userService: UserService
) {

    fun execute(name: String?): StudentsResponse {
        val user = userService.getCurrentUser()
        val students = studentService.getAllStudentsByName(name, user.schoolId)

        return StudentsResponse.of(students)
    }
}
