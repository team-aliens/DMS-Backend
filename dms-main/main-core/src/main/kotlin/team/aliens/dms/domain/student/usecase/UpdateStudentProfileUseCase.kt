package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService

@UseCase
class UpdateStudentProfileUseCase(
    private val studentService: StudentService
) {

    fun execute(profileImageUrl: String) {

        val student = studentService.getCurrentStudent()

        studentService.saveStudent(
            student.copy(profileImageUrl = profileImageUrl)
        )
    }
}
