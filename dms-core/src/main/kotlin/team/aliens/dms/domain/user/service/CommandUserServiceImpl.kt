package team.aliens.dms.domain.user.service

import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.CommandUserPort


class CommandUserServiceImpl(
    private val commandUserPort: CommandUserPort
) : CommandUserService {
    override fun saveUser(user: User) =
        commandUserPort.saveUser(user)
}