package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class CheckAccountIdExistenceUseCase(
    private val userService: UserService
) {

    fun execute(accountId: String): String {
        val user = userService.queryUserByAccountId(accountId)
        return StringUtil.coveredEmail(user.email)
    }
}
