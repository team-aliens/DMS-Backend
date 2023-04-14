package team.aliens.dms.domain.user.service

import java.util.UUID
import team.aliens.dms.domain.user.model.User

interface CommandUserService {
    fun saveUser(user: User): User
    fun deleteUserById(userId: UUID)
}