package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.UserService

@UseCase
class StudentWithdrawalUseCase(
    private val studentService: StudentService,
    private val userService: UserService
) {

    fun execute() {

        val student = studentService.getCurrentStudent()
        student.userId ?: throw UserNotFoundException

        userService.deleteUserById(student.userId)
    }
}
