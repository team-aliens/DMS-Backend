package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface QueryUserPort {

    fun queryUserById(userId: UUID): User?

    fun queryUserByEmail(email: String): User?

    fun queryUserByAccountId(accountId: String): User?

    fun queryUserBySchoolIdAndAuthority(schoolId: UUID, authority: Authority): User?

    fun queryUsersBySchoolId(schoolId: UUID): List<User>

    fun existsUserByEmail(email: String): Boolean

    fun existsUserByAccountId(accountId: String): Boolean
}
