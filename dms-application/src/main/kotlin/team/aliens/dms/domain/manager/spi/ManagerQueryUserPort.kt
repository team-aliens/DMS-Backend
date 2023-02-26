package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface ManagerQueryUserPort {

    fun queryUserById(userId: UUID): User?

    fun queryUserBySchoolIdAndAuthority(schoolId: UUID, authority: Authority): User?

    fun queryUserByAccountId(accountId: String): User?

    fun queryUserByEmail(email: String): User?
}
