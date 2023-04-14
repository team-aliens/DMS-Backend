package team.aliens.dms.domain.user.service

import team.aliens.dms.domain.user.model.User

interface CommandUserService {
    fun saveUser(user: User): User
}