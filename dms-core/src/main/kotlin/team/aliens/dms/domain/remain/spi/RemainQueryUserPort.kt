package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface RemainQueryUserPort {

    fun queryUserById(userId: UUID): User?
}
