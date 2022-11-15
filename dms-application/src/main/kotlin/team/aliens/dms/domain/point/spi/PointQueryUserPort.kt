package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface PointQueryUserPort {

    fun queryUserById(userId: UUID): User?

}