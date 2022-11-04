package team.aliens.dms.domain.notice.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface NoticeQueryUserPort {

    fun queryUserById(userId: UUID): User?

}