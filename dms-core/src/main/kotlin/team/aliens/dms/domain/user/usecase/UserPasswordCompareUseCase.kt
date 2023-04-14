package team.aliens.dms.domain.user.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.service.SecurityService
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class UserPasswordCompareUseCase(
    private val getUserService: GetUserService,
    private val securityService: SecurityService
) {

    fun execute(password: String) {
        val user = getUserService.getCurrentUser()
        securityService.checkIsPasswordMatches(password, user.password)
    }
}
