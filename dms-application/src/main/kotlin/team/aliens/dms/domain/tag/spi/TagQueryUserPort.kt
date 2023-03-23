package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface TagQueryUserPort {

    fun queryUserById(userId: UUID): User?
}
