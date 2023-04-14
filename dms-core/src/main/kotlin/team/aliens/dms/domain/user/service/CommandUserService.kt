package team.aliens.dms.domain.user.service

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface CommandUserService {
    fun saveUser(user: User): User
    fun deleteUserById(userId: UUID)
}
