package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.UserService

@UseCase
class StudentWithdrawalUseCase(
    private val userService: UserService
) {

    fun execute() {

        val student = userService.getCurrentStudent()
        student.userId ?: throw UserNotFoundException

        userService.deleteUserById(student.userId)
    }
}
