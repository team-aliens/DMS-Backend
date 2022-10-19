package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.user.model.User

interface CommandUserPort {
    fun saveUser(user: User)
}