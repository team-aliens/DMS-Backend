package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveStudentUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val remainService: RemainService,
) {

    fun execute(studentId: UUID) {

        val student = studentService.getStudentById(studentId)

        remainService.deleteRemainStatusByStudentId(studentId)

        studentService.deleteStudent(student)
        student.userId?.let { userService.deleteUserById(it) }
    }
}
