package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface SchoolQueryUserPort {

    fun queryUserById(userId: UUID): User?
}
