package team.aliens.dms.domain.file.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface FileQueryUserPort {
    fun queryUserById(userId: UUID): User?
}
