package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface StudentQueryUserPort {

    fun existsUserByEmail(email: String): Boolean

    fun existsUserByAccountId(accountId: String): Boolean

    fun queryUserById(userId: UUID): User?

    fun queryUserByAccountId(accountId: String): User?

}