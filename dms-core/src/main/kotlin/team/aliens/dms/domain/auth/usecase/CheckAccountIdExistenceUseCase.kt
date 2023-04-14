package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class CheckAccountIdExistenceUseCase(
    private val getUserService: GetUserService
) {

    fun execute(accountId: String): String {
        val user = getUserService.queryUserByAccountId(accountId)
        return StringUtil.coveredEmail(user.email)
    }
}
