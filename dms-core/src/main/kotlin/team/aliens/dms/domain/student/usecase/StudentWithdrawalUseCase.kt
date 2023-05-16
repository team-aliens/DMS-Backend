package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class StudentWithdrawalUseCase(
    private val securityService: SecurityService,
    private val userService: UserService
) {

    fun execute() {
        val userId = securityService.getCurrentUserId()
        userService.deleteUserById(userId)
    }
}
