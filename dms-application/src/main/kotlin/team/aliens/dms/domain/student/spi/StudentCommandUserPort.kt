package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.user.model.User

interface StudentCommandUserPort {
    fun saveUser(user: User)
}