package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface QueryUserPort {

    fun queryUserById(userId: UUID): User?

}