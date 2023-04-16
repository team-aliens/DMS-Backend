package team.aliens.dms.domain.user.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException
import team.aliens.dms.domain.user.exception.UserEmailExistsException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@Service
class CheckUserServiceImpl(
    private val queryUserPort: QueryUserPort
) : CheckUserService {

    override fun checkUserNotExistsByEmail(email: String) {
        if (queryUserPort.existsUserByEmail(email)) {
            throw UserEmailExistsException
        }
    }

    override fun checkUserExistsByEmail(email: String) {
        if (!queryUserPort.existsUserByEmail(email)) {
            throw UserNotFoundException
        }
    }

    override fun checkUserNotExistsByAccountId(accountId: String) {
        if (queryUserPort.existsUserByAccountId(accountId)) {
            throw UserAccountIdExistsException
        }
    }
}
