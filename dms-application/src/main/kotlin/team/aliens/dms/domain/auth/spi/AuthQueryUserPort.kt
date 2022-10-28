package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.user.model.User

interface AuthQueryUserPort {

    fun queryUserByEmail(email: String) : User?

    fun queryUserByAccountId(accountId: String) : User?

}