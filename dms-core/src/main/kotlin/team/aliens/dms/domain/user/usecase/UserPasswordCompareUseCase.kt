package team.aliens.dms.domain.user.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.service.SecurityService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class UserPasswordCompareUseCase(
    private val userService: UserService,
    private val securityService: SecurityService
) {

    fun execute(password: String) {
        val user = userService.getCurrentUser()
        securityService.checkIsPasswordMatches(password, user.password)
    }
}
