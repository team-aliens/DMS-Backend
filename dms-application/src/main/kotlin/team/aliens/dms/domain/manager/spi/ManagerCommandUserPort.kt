package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.user.model.User

interface ManagerCommandUserPort {
    fun saveUser(user: User): User
}