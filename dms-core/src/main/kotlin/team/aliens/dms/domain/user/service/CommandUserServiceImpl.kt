package team.aliens.dms.domain.user.service

import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.CommandUserPort
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.time.LocalDateTime
import java.util.UUID
import team.aliens.dms.common.annotation.Service

@Service
class CommandUserServiceImpl(
    private val queryUserPort: QueryUserPort,
    private val commandUserPort: CommandUserPort
) : CommandUserService {

    override fun saveUser(user: User) =
        commandUserPort.saveUser(user)

    override fun deleteUserById(userId: UUID) {
        val user = queryUserPort.queryUserById(userId) ?: throw UserNotFoundException
        commandUserPort.saveUser(
            user.copy(deletedAt = LocalDateTime.now())
        )
    }
}
