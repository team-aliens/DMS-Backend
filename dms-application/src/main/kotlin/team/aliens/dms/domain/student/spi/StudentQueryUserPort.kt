package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface StudentQueryUserPort {
    fun existsByEmail(email: String): Boolean
    fun existsByAccountId(accountId: String): Boolean
    fun queryByUserId(id: UUID): User?
    fun queryByAccountId(accountId: String): User?
}